package server.businessRules;

import server.data.player.EPlayerGameStatus;
import server.data.player.PlayerStatus;
import server.exceptions.BusinessRuleViolationException;

public class PlayerMaySendMove implements IPlayerRule {
	
	private PlayerStatus player;
	
	public PlayerMaySendMove(PlayerStatus player) {
        this.player = player;
    }

	@Override
	public boolean enforce() {
		if (player.getPlayerGameState() == EPlayerGameStatus.MustMove) {
            return true;
        }
		else 
		 throw new BusinessRuleViolationException("PlayerMayNotSendMove", "Player tries to send Move" );
	}

}
