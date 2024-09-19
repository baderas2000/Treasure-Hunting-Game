package server.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import server.businessRules.HalfMapHasEnoughTerrainTypes;
import server.businessRules.HalfMapHasEnoughTiles;
import server.businessRules.HalfMapHasNoIslands;
import server.businessRules.HalfMapHasNoWaterOnBorders;
import server.businessRules.HalfMapHasOneFort;
import server.businessRules.HalfMapHasValidBordersLength;
import server.businessRules.IMapRule;
import server.data.game.GameEntity;
import server.data.game.GameStatus;
import server.data.map.EMapMergeType;
import server.data.map.FullMap;
import server.data.map.GameMap;
import server.exceptions.BusinessRuleViolationException;

public class MapManager {
	
	final static Logger logger = LoggerFactory.getLogger(MapManager.class);
	private GameEntity currentGame;

	public MapManager (GameEntity game) {
		this.currentGame = game;
	}

	public boolean validateMapHalf(GameMap mapHalf) {
		boolean result;
		try {
			result = this.checkHalfMapBusinessRules(mapHalf);
		}catch(BusinessRuleViolationException e) {
			result = false;
			throw new BusinessRuleViolationException("Map Business Rule Violation: " , e.getMessage());
		}
		return result;
	}
	
	public boolean checkHalfMapBusinessRules(GameMap mapHalf) {
		List<IMapRule> rules = Arrays.asList(new HalfMapHasEnoughTerrainTypes(mapHalf), new HalfMapHasEnoughTiles(mapHalf), 
				new HalfMapHasNoIslands(mapHalf), new HalfMapHasNoWaterOnBorders(mapHalf), 
				new HalfMapHasOneFort(mapHalf), new HalfMapHasValidBordersLength(mapHalf));
		return rules.stream().allMatch(IMapRule::enforce);	
    }

	public void setFullMap(GameMap mapHalf) {
		GameStatus gameContent = currentGame.getGameStatus();
		if(gameContent.getMap() == null) {
			logger.info("Map was empty");
			gameContent.setFullMap(mapHalf);
		}
		else {
			logger.info("Map is to be extended");
			FullMap fullMap = this.extendMap(gameContent.getMap(), mapHalf);
			gameContent.setFullMap(fullMap);
		}
		gameContent.changeGameStateId();
	}
	
	public FullMap extendMap(GameMap oldMapHalf, GameMap mapHalf) {
		ArrayList<GameMap> mapHalves = new ArrayList<GameMap>();
		mapHalves.add(oldMapHalf);
		mapHalves.add(mapHalf);
		
		Random random = new Random();
		int randomIndex = random.nextInt(mapHalves.size());
		FullMap firstHalfMap = (FullMap) mapHalves.get(randomIndex);
		mapHalves.remove(randomIndex);
		FullMap secondHalfMap = (FullMap) mapHalves.stream().findAny().get();
		FullMap fullMap = firstHalfMap;
		
		if(pickRandomForm() == EMapMergeType.ExtendOn_X_Side) {
			extendXSide(secondHalfMap);
		}
		else {
			extendYSide(secondHalfMap);
			}
		secondHalfMap.stream().forEach(t->{
			fullMap.addTile(t);
		});	
		return fullMap;
	}
	
	private void extendYSide(FullMap mapHalf) {
		mapHalf.stream().forEach(t->{
			switch (t.getY()) {
	        case 0:
	        	t.setY(5);
	            break;
	        case 1:
	        	t.setY(6);
	            break;
	        case 2:
	        	t.setY(7);
	            break;
	        case 3:
	        	t.setY(8);
	            break;
	        case 4:
	        	t.setY(9);
	            break;
	    }
		});
	}

	private void extendXSide(FullMap mapHalf) {
		mapHalf.stream().forEach(t->{
			switch (t.getX()) {
	        case 0:
	        	t.setX(10);
	            break;
	        case 1:
	        	t.setX(11);
	            break;
	        case 2:
	        	t.setX(12);
	            break;
	        case 3:
	        	t.setX(13);
	            break;
	        case 4:
	        	t.setX(14);
	            break;
	        case 5:
	        	t.setX(15);
	            break;
	        case 6:
	        	t.setX(16);
	            break;
	        case 7:
	        	t.setX(17);
	            break;
	        case 8:
	        	t.setX(18);
	            break;
	        case 9:
	        	t.setX(19);
	            break;
	    }
		});	
	}

	private EMapMergeType pickRandomForm() {
			EMapMergeType[] forms = EMapMergeType.values();
			Random random = new Random();
			int randomIndex = random.nextInt(forms.length);
			return forms[randomIndex];
		}

}
