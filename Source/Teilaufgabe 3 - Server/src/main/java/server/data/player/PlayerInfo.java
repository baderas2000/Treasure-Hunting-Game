package server.data.player;

public class PlayerInfo {
	private String studentFirstName;
	private String studentLastName;
	private String studentAccount;
	
	public PlayerInfo(String studentFirstName, String studentLastName, String studentAccount) {
		this.studentFirstName = studentFirstName;
		this.studentLastName = studentLastName;
		this.studentAccount = studentAccount;
	}
	
	public String getStudentFirstName() {
		return studentFirstName;
	}
	
	public String getStudentLastName() {
		return studentLastName;
	}
	
	public String getStudentAccount() {
		return studentAccount;
	}
	
	@Override
	public boolean equals (Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (!(o instanceof PlayerInfo))
			return false;
		PlayerInfo playerInfo = (PlayerInfo) o;
		return this.studentFirstName.equals(playerInfo.studentFirstName) &&
			this.studentLastName.equals(playerInfo.studentLastName) &&
			this.studentAccount.equals(playerInfo.studentAccount);
	}
	
	@Override
	public int hashCode () {
		return new StringBuilder()
			.append(this.studentFirstName)
			.append(this.studentLastName)
			.append(this.studentAccount)
			.toString()
			.hashCode();
	}
}
