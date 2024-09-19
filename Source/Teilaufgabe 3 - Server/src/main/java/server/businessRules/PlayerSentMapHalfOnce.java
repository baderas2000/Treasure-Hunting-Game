package server.businessRules;

import server.data.player.PlayerStatus;
import server.exceptions.BusinessRuleViolationException;

public class PlayerSentMapHalfOnce implements IPlayerRule {
	
	private PlayerStatus player;
	
	public PlayerSentMapHalfOnce(PlayerStatus player) {
        this.player = player;
    }

	@Override
	public boolean enforce() {
		if (!(player.isMapHalfSent())) {
			return true;
		}
		else {
			throw new BusinessRuleViolationException("PlayerMayNotSendMapHalfTwice", "Player tries to send Map Half second time" );
		}
	}
}
