import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class TaskDataBase {
	
	private String filename;
	private ArrayList<Task> tasks;
	
	
	public TaskDataBase(String filename) {
		this.filename = filename;
		tasks = new ArrayList<Task>();
		File file = new File(filename);
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			
			String line;
			String title, status, description;
			
			while ((line = reader.readLine()) != null) {
				
				int id = Integer.parseInt(line);
				line = reader.readLine();
				title = line != null ? line : "";
				line = reader.readLine();
				status = line != null ? line : "";
				line = reader.readLine();
				description = line != null ? line : "";
				
				Task task = new Task(id, title, Task.TASK_STATUS.valueOf(status), description);
				tasks.addLast(task);
				
			}
			
		} catch (IOException e) {
			System.out.println("Ocurrio un error al intentar leer el archivo.");
			e.printStackTrace();
		}
		
	}
	
	public int getLastIndex() {
		if (tasks.size() == 0) {
			return 0;
		}
		return tasks.getLast().getID();
	}
	
	public void addTask(Task task) {
		tasks.add(task);
	}
	
	public void deleteTaskById(int id) {
		for (int i = 0; i < tasks.size(); ++i) {
			if (tasks.get(i).getID() == id) {
				tasks.remove(i);
				return;
			}
		}
	}
	
	public Task getTaskById(int id) {
		for (Task task : tasks) {
			if (task.getID() == id) {
				return task;
			}
		}
		return null;
	}
	
	public Task[] getTasks() {
		Task [] tasksArray = new Task[tasks.size()];
		for (int i = 0; i < tasks.size(); ++i) {
			tasksArray[i] = tasks.get(i).clone();
		}
		return tasksArray;
	}
	
	public void saveAndClose() {
		//System.out.println("Guardando tareas...");
		try (PrintWriter writer = new PrintWriter(new FileWriter(this.filename))) {
			//System.out.println("Dentro de try");
			//System.out.println("tasks.size(): " + tasks.size());
			for (Task task : tasks) {
				//System.out.println("\t" + task);
				writer.write(task.getID() + "\n");
				writer.write(task.getTitle() + "\n");
				writer.write(task.getStatus().name() + "\n");
				writer.write(task.getDescription() + "\n");
			}
			//System.out.println("Tareas guardadas.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}