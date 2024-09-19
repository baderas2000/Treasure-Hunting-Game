package server.businessRules;

import server.data.player.EPlayerGameStatus;
import server.data.player.PlayerStatus;
import server.exceptions.BusinessRuleViolationException;

public class PlayerMaySendMapHalf implements IPlayerRule {
	
	private PlayerStatus player;
	
	public PlayerMaySendMapHalf(PlayerStatus player) {
        this.player = player;
    }

	@Override
	public boolean enforce() {
		 if (player.getPlayerGameState() == EPlayerGameStatus.MustSentMap) {
	            return true;
	        }
		 else {
			 throw new BusinessRuleViolationException("PlayerMayNotSendMapHalf", "Player tries to send Map Half, but it is not their turn." );
		 }
	}

}
