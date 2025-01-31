public class Task {
	
	private final int ID;
	
	private String title;
	
	private TASK_STATUS status;
	
	private String description;
	
	public static enum TASK_STATUS {
		TO_DO,
		IN_PROGRESS,
		DONE
	}
	
	public static boolean isValidStatus(String status) {
		for (TASK_STATUS task_status : TASK_STATUS.values()) {
			//System.out.println("task_status.name() == status: " + task_status.name() == status);
			//System.out.println("task_status.name().compareTo(status): " + task_status.name().compareTo(status));
			if (task_status.name().compareTo(status) == 0) {
				return true;
			}
		}
		return false;
	}
	
	public Task(int id, String title, TASK_STATUS status, String description) {
		this.ID = id;
		this.title = title;
		this.status = status;
		this.description = description;
	}
	
	public int getID() {
		return this.ID;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public TASK_STATUS getStatus() {
		return this.status;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setTitle(String newTitle) {
		this.title = newTitle;
	}
	
	public void setStatus(TASK_STATUS newStatus) {
		this.status = newStatus;
	}
	
	public void setDescription(String newDescription) {
		this.description = newDescription;
	}
	
	public Task clone() {
		return new Task(ID, title, status, description);
	}
	
	public String toString() {
		return ID + " - " + title + " - " + status + " - " + description;
	}
}