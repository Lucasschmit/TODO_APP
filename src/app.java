import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class app {
	
	private static TaskDataBase database;
	
	private static final ArrayList<String>  comandos = new ArrayList<String>(Arrays.asList(
		"add",
		"update",
		"delete",
		"mark-in-progress",
		"mark-done",
		"list",
		"list-done",
		"list-not-done",
		"list-in-progress",
		"list-to-do",
		"help"
	));
	
	private static void initializeDatabase() {
		database = new TaskDataBase("todo_app.txt");
	}
	
	private static void printHelp() {
		System.out.println("Mostrando ayuda:");
	}
	
	private static void printHelp(String error) {
		System.out.println(error);
		System.out.println();
		printHelp();
	}
	
	private static int addTask(String title, String status, String description) {
		initializeDatabase();
		int taskId = database.getLastIndex() + 1;
		
		System.out.println("Creando tarea:");
		System.out.println(taskId);
		System.out.println(title);
		System.out.println(status);
		System.out.println(description);
		
		Task newTask = new Task(taskId, title, Task.TASK_STATUS.valueOf(status), description);
		
		database.addTask(newTask);
		
		return taskId;
	}
	
	private static void update(int id, boolean modifyTitle, String title, boolean modifyStatus, String status, boolean modifyDescription, String description) {
		initializeDatabase();
		Task taskToModify = database.getTaskById(id);
		
		if (taskToModify != null) {
			if (modifyTitle) {
				taskToModify.setTitle(title);
			}
			if (modifyStatus) {
				taskToModify.setStatus(Task.TASK_STATUS.valueOf(status));
			}
			if (modifyDescription) {
				taskToModify.setDescription(description);
			}
		}
	}
	
	public static void listAll() {
		initializeDatabase();
		System.out.println("Mostrando todas las tareas:");
		for (Task t : database.getTasks()) {
			System.out.println("\t" + t);
		}
	}
	
	public static void listByStatus(String status) {
		initializeDatabase();
		Task[] tasks = database.getTasks();
		boolean seEncontraonTareas = false;
		for (Task t : tasks) {
			if (t.getStatus() == Task.TASK_STATUS.valueOf(status)) {
				seEncontraonTareas = true;
				System.out.println("\t" + t);
			}
		}
		if (!seEncontraonTareas) {
			System.out.println("No se encontraron tareas con status = " + status);
		}
	}
	
	public static void delete(int id) {
		initializeDatabase();
		database.deleteTaskById(id);
		System.out.println("Tarea borrada.");
	}

	public static void main(String[] args) {
		if (args.length < 1) {
			printHelp("Error: cantidad de argumentos insuficiente.");
			return;
		} else if (!comandos.contains(args[0])) {
			printHelp("Error: Comando desconocido.");
		}
		
		Iterator<String> args_iterator = Arrays.asList(args).iterator();
		String command = args_iterator.next(), title = "", status = "", description = "";
		int id = 0;
		
		
		
		
		//Seleccionar comando
		switch(command) {
		case "add":
			if (args_iterator.hasNext()) {
				title = args_iterator.next();
				if (args_iterator.hasNext()) {
					status = args_iterator.next();
					if (!Task.isValidStatus(status)) {
						printHelp("Error: valor para 'status' invalido. status ingresado: " + status + ". status validos: " + Arrays.asList(Task.TASK_STATUS.values()));
						
						//System.out.println("status.length(): " + status.length());
						//System.out.println("Task.TASK_STATUS.TO_DO.name().length(): " + Task.TASK_STATUS.TO_DO.name().length());
						return;
					}
					if (args_iterator.hasNext()) {
						description = args_iterator.next();
					}
				}
				
				if (status.isBlank()) {
					status = Task.TASK_STATUS.TO_DO.name();
				}
				
				int taskId = addTask(title, status, description);
				
				if (taskId != -1) {
					System.out.println("Tarea agregada. ID: " + taskId);
				}
			} else {
				printHelp("Error en ´add´: cantidad de argumentos insuficiente.");
				return;
			}
			break;
		case "update":
			if (args_iterator.hasNext()) {
				String id_string = args_iterator.next();
				boolean modifyTitle = false, modifyStatus = false, modifyDescription = false;
				try {
					id = Integer.parseInt(id_string);
					if (!args_iterator.hasNext()) {
						printHelp("Error: Faltan argumentos.");
					}
					while (args_iterator.hasNext()) {
						String arg = args_iterator.next();
						if (args_iterator.hasNext()) {
							switch(arg) {
							case "-title":
								title = args_iterator.next();
								modifyTitle = true;
								break;
							case "-status":
								String possibleStatus = args_iterator.next();
								if (Task.isValidStatus(possibleStatus)) {
									status = possibleStatus;
									modifyStatus = true;
								} else {
									printHelp("Error: argumento invalido para estado: " + possibleStatus);
									break;
								}
								break;
							case "-description":
								description = args_iterator.next();
								modifyDescription = true;
								break;
							default:
								printHelp("Error: argumento invalido: " + args);
								break;
							}
						}
					}
				} catch (NumberFormatException e) {
					printHelp("Error. Formato incorrecto de identificador de tarea.");
				}
				
				update(id, modifyTitle, title, modifyStatus, status, modifyDescription, description);
				
			} else {
				printHelp("Error: Faltan argumentos");
			}
			
			
			break;
		case "delete":
			if (!args_iterator.hasNext()) {
				printHelp("Error: Cantidad insuficiente de argumentos");
				return;
			}
			try {
				id = Integer.parseInt(args_iterator.next());
				delete(id);
			} catch (NumberFormatException e) {
				printHelp("Error. Formato incorrecto de identificador de tarea.");
				return;
			}
			
			break;
		case "mark-in-progress":
			if (args_iterator.hasNext()) {
				try {
					id = Integer.parseInt(args_iterator.next());
					update(id, false, title, true, Task.TASK_STATUS.IN_PROGRESS.name(), false, description);
				} catch (NumberFormatException e) {
					printHelp("Error. Formato incorrecto de identificador de tarea.");
				}
			} else {
				printHelp("Error: Cantidad insuficiente de argumentos.");
				return;
			}
			break;
		case "mark-done":
			if (args_iterator.hasNext()) {
				try {
					id = Integer.parseInt(args_iterator.next());
					update(id, false, title, true, Task.TASK_STATUS.DONE.name(), false, description);
				} catch (NumberFormatException e) {
					printHelp("Error. Formato incorrecto de identificador de tarea.");
				}
			} else {
				printHelp("Error: Cantidad insuficiente de argumentos.");
				return;
			}
			break;
		case "list":
			listAll();
			break;
		case "list-done":
			listByStatus("DONE");
			break;
		case "list-not-done":
			listByStatus("TO_DO");
			listByStatus("IN_PROGRESS");
			break;
		case "list-in-progress":
			listByStatus("IN_PROGRESS");
			break;
		case "list-to-do":
			listByStatus("TO_DO");
			break;
		case "help":
			printHelp();
			break;
		default:
			printHelp("Error inesperado. Comando invalido: " + command);
			break;
			
		}
		
		database.saveAndClose();
	}

}
