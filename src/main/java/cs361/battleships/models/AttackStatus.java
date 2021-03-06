package cs361.battleships.models;

public enum AttackStatus {

	/**
	 * The result if an attack results in a miss.
	 */
	MISS,

	/**
	 * The result if an attack results in a hit on an enemy ship.
	 */
	HIT,

	/**
	 * The result if an attack sinks the enemy ship
	 */
	SUNK,

	/**
	 * The results if an attack results in the defeat of the opponent (a
	 * surrender).
	 */
	SURRENDER,

	/**
	 * The results if an attack results in hitting a captains quarter.
	 */
	CAPTAINHIT,

	/**
	 * The result if the coordinates given are invalid.
	 */
	INVALID,

	/**
	 * The results if sonar selection results in a occupied square.
	 */
	SONAROCCUPIED,

	/**
	 * The results if sonar selection results in a non occupied square
	 */
	SONARNOTOCCUPIED,
}
