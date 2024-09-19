package server.data.map;

import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import server.data.player.PlayerIdentifier;

public class FullMapTile extends GameMapTile {

	private Optional<PlayerIdentifier> fortOwner;
	private Optional<PlayerIdentifier> treasureOwner;
	private Set<PlayerIdentifier> presentPlayers = new HashSet<PlayerIdentifier>();
	
	private EFortStatus fortState;
	private ETreasureStatus treasureState;

	public FullMapTile (int x, int y, ETerrainType terrainType,
			PlayerIdentifier fortOwner, PlayerIdentifier treasureOwner,
			EFortStatus fortState, ETreasureStatus treasureState,
			PlayerIdentifier playerPositionOwner) {
		super(x, y, terrainType);
		this.fortOwner = Optional.ofNullable(fortOwner);
		this.treasureOwner = Optional.ofNullable(treasureOwner);
		this.fortState = fortState;
		this.treasureState = treasureState;
		if(playerPositionOwner != null) {
			this.presentPlayers.add(playerPositionOwner);
		}
	}
	
	public FullMapTile (FullMapTile mapTile) {
		super(mapTile);
		this.fortOwner = mapTile.fortOwner;
		this.treasureOwner = mapTile.treasureOwner;
		this.presentPlayers = mapTile.presentPlayers;
		this.fortState = mapTile.fortState;
		this.treasureState = mapTile.treasureState;
		this.presentPlayers = mapTile.presentPlayers;
	}

	public Optional<PlayerIdentifier> getFortOwner() {
		return this.fortOwner;
	}
	
	public Optional<PlayerIdentifier> getTreasureOwner() {
		return this.treasureOwner;
	}
	
	public List<PlayerIdentifier> getPresentPlayers () {
		return new ArrayList<PlayerIdentifier>(this.presentPlayers);
	}
	
	public boolean hasPlayerPresent (PlayerIdentifier playerID) {
		ArrayList<PlayerIdentifier> arrayList = new ArrayList<>();
		for (PlayerIdentifier playerIdentifier : presentPlayers) {
		    arrayList.add(playerIdentifier);
		}

		if(presentPlayers.isEmpty()) {return false;}
		PlayerIdentifier foundPlayer = presentPlayers.stream()
				.filter(player -> player.getPlayerIdentifier().equals(playerID.getPlayerIdentifier()))
				.findFirst()
				.orElse(null);
		
		if(foundPlayer != null) {
			return true;
		}
	    return false;
	}
	
	public boolean addPlayer (PlayerIdentifier playerID) {
		if (!this.presentPlayers.contains(playerID))
			return this.presentPlayers.add(playerID);
		return false;
	}
	
	public boolean removePlayer (PlayerIdentifier playerID) {
		return this.presentPlayers.remove(playerID);
	} 
	
	public EFortStatus getFortState() {
		return fortState;
	}
	
	public ETreasureStatus getTreasureState() {
		return treasureState;
	}
	
	@Override
	public boolean equals (Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (!(o instanceof FullMapTile))
			return false;
		FullMapTile tile = (FullMapTile) o;
		return super.equals(tile) &&
			this.fortOwner.equals(tile.fortOwner) &&
			this.treasureOwner.equals(tile.treasureOwner) &&
			this.presentPlayers.equals(tile.presentPlayers);
	}
}
