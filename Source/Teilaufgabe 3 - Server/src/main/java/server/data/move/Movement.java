package server.data.move;

import java.util.Optional;

/**
 * Provides functionality to capture information about movement of certain player.
 * Holds information about amount of moves performed in specific direction until 
 * player position is changed and direction of movement.
 * 
 * @author Sofiia Badera
 *
 */
public class Movement {

	private int counter = 0;
	private EMove moveDirection;
	
	public int getCounter () {
		return this.counter;
	}
	
	public Optional<EMove> getMoveDirection () {
		return Optional.ofNullable(this.moveDirection);
	}
	
	/**
	 * Method updated <code>counter</code> and <code>moveDirection</code> in accordance with
	 * business rules that define how moves are performed. 
	 * 
	 * @param move Direction in which move was performed.
	 * @throws RuntimeException is provided move is null.
	 * @return True if direction of movement changed else false.
	 */
	public boolean performMove (EMove move) {
		if (move == null) {
			throw new RuntimeException("Can not perform null move.");
		}
		boolean directionChanged = this.moveDirection != move;
		if (directionChanged) {
			this.moveDirection = move;
			this.counter = 1;
		} else {
			this.counter++;
		}
		return directionChanged;
	}
	
	/**
	 * Methods clears internal state by setting counter to zero and
	 * moveDirection to null as if no move was performed previously.
	 * 
	 * @return True if state has changed otherwise false.
	 */
	public boolean clear () {
		boolean stateChanged = (this.counter != 0 || this.moveDirection != null);
		this.counter = 0;
		this.moveDirection = null;
		return stateChanged;
	}
	
}
