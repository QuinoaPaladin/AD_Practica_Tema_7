package academia;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.matisse.MtDatabase;
import com.matisse.MtException;
import com.matisse.MtObjectIterator;

public class Principal
{

	public static void main(String[] args) 

	{
		String hostname = "localhost";
		String dbname = "academia";
		
		creaObjetos(hostname, dbname);

		modificaObjeto(hostname, dbname, "Álvaro", 605061424);
		ejecutaOQL(hostname, dbname);

		borrarTodos(hostname, dbname);
	}


	public static void creaObjetos(String hostname, String dbname) 
	{
		try 
		{
			
			MtDatabase db = new MtDatabase(hostname, dbname);
			db.open();
			db.startTransaction();
			System.out.println("Conectado a la base de datos " + db.getName() + " de Matisse");
			System.out.println("\n");

			// Crea un objeto Profesores
			Profesores p1 = new Profesores(db);
			p1.setNombre("Álvaro");
			p1.setApellidos("Ramírez Barcia");
			p1.setTelefono(605065815);
			p1.setDni("53965541H");
			System.out.println("Objeto de tipo Profesores creado.");

			// Crea un objeto Asignaturas
			Asignaturas a1 = new Asignaturas(db);
			a1.setNombre("Literatura Universal");
			a1.setAula(4);
			a1.setDuracion(1);
			a1.setHoraInicio("10:00");
			a1.setDiaSemana("Lunes");
			System.out.println("Objeto de tipo Asignaturas creado.");

			// Crea un objeto Cursos
			GregorianCalendar gc = new GregorianCalendar();
			gc.set(GregorianCalendar.YEAR, 2022);
			gc.set(GregorianCalendar.MONTH, 3);
			gc.set(GregorianCalendar.DATE, 16);

			Cursos cu1 = new Cursos(db);
			cu1.setNombre("Literatura Universal");
			cu1.setAula(4);
			cu1.setDuracion(1);
			cu1.setHoraInicio("10:00");
			cu1.setFecha(gc);
			System.out.println("Objeto de tipo Cursos creado.");
			
			Clases c1[] = new Clases[2];
			c1[0] = a1;
			c1[1] = cu1;

			// Guarda las relaciones del autor con los libros que ha escrito.
			p1.setImparte(c1);
			// Ejecuta un commit para materializar las peticiones.
			db.commit();
			// Cierra la base de datos.
			db.close();
			System.out.println("\nSe ha guardado todo en la base de datos.");
			System.out.println("\n");
		} 
		
		catch (MtException mte) 
		{
			System.out.println("MtException : " + mte.getMessage());
		}
	}

	public static void modificaObjeto(String hostname, String dbname, String nombre, Integer nuevaEdad) 
	{
		System.out.println("=========== Modificación un objeto ==========\n");

		int nProfesores = 0;
		try 
		{
			MtDatabase db = new MtDatabase(hostname, dbname);
			db.open();
			db.startTransaction();

			System.out.println("\n" + Profesores.getInstanceNumber(db) + " objetos de tipo Profesores en la DB.");
			nProfesores = (int) Profesores.getInstanceNumber(db);

			MtObjectIterator<Profesores> iter = Profesores.<Profesores>instanceIterator(db);

			while (iter.hasNext()) 
			{
				Profesores[] profesores = iter.next(nProfesores);

				for (int i = 0; i < profesores.length; i++) 
				{
					if (profesores[i].getNombre().compareTo(nombre) == 0) 
					{
						profesores[i].setTelefono(nuevaEdad);
					} 
					else 
					{
						System.out.println("No se ha encontrado ningún profesor de nombre " + nombre + " en la base de datos " + db.getName() + ".");
					}
				}
			}
			iter.close();
			db.commit();
			db.close();
			System.out.println("\nModificación del objeto finalizada correctamente.");
			System.out.println("\n");
		} 
		catch (MtException mte) 
		{
			System.out.println("MtException : " + mte.getMessage());
		}
	}

	public static void ejecutaOQL(String hostname, String dbname) 
	{
		MtDatabase dbcon = new MtDatabase(hostname, dbname);
		dbcon.open();				
		System.out.println("=========== Consultar todos los objetos ==========\n");
		try 
		{			
			Statement stmt = dbcon.createStatement();
			String commandText = "SELECT REF(a) from academia.Profesores a;";
			ResultSet rset = stmt.executeQuery(commandText);
			Profesores p1;
			while (rset.next()) 
			{
				p1 = (Profesores) rset.getObject(1);
				System.out.println("Los valores de los atributos del objeto de tipo Profesores son: " + p1.getNombre() + ", "	+ p1.getApellidos() + ", " +	p1.getTelefono()  + ", "	+ p1.getDni() + ".");
				System.out.println("\n");
			}	
			rset.close();
			stmt.close();			
		} 
		catch (SQLException e) 
		{
			System.out.println("SQLException: " + e.getMessage());
		}		
		try 
		{			
			Statement stmt = dbcon.createStatement();
			String commandText = "SELECT REF(a) from academia.Clases a;";
			ResultSet rset = stmt.executeQuery(commandText);
			Clases c1;
			while (rset.next()) 
			{
				c1 = (Clases) rset.getObject(1);
				System.out.println("Los valores de los atributos del objeto de tipo Clases son: " + c1.getNombre() + ", "	+ c1.getAula() + ", " +	c1.getDuracion()  + ", "	+ c1.getHoraInicio() + ".");
				System.out.println("\n");
			}	
			rset.close();
			stmt.close();			
		} 
		catch (SQLException e) 
		{
			System.out.println("SQLException: " + e.getMessage());
		}
		
		try 
		{			
			Statement stmt = dbcon.createStatement();
			String commandText = "SELECT REF(a) from academia.Asignaturas a;";
			ResultSet rset = stmt.executeQuery(commandText);
			Asignaturas a1;
			while (rset.next()) 
			{
				a1 = (Asignaturas) rset.getObject(1);
				System.out.println("Los valores de los atributos del objeto de tipo Asignaturas son: " + a1.getNombre() + ", "	+ a1.getAula() + ", " +	a1.getDuracion()  + ", "	+ a1.getHoraInicio() +  ", "	+ a1.getDiaSemana() + ".");
				System.out.println("\n");
			}	
			rset.close();
			stmt.close();			
		} 
		catch (SQLException e) 
		{
			System.out.println("SQLException: " + e.getMessage());
		}
		
		try 
		{
			
			Statement stmt = dbcon.createStatement();
			String commandText = "SELECT REF(a) from academia.Cursos a;";
			ResultSet rset = stmt.executeQuery(commandText);
			Cursos cu1;
			while (rset.next()) 
			{
				cu1 = (Cursos) rset.getObject(1);
				System.out.println("Los valores de los atributos del objeto de tipo Cursos son: " + cu1.getNombre() + ", "	+ cu1.getAula() + ", " +	cu1.getDuracion()  + ", "	+ cu1.getHoraInicio() + ", "	+ cu1.getFecha().toZonedDateTime() + ".");
				System.out.println("\n");
			}	
			rset.close();
			stmt.close();			
		} 
		catch (SQLException e) 
		{
			System.out.println("SQLException: " + e.getMessage());
		}
		
		
	}

	public static void borrarTodos(String hostname, String dbname) 
	{
		System.out.println("====================== Borrar todos los objetos de todas las clases	=====================\n");

		try 
		{
			MtDatabase db = new MtDatabase(hostname, dbname);
			db.open();
			db.startTransaction();

			System.out.println("\n" + Profesores.getInstanceNumber(db) + " objetos de tipo Profesores encontrados en la BD.");
			System.out.println("\n" + Clases.getInstanceNumber(db) + " objetos de tipo Clases encontrados en la BD.");
			System.out.println("\n" + Asignaturas.getInstanceNumber(db) + " objetos de tipo Asignaturas encontrados en la BD.");
			System.out.println("\n" + Cursos.getInstanceNumber(db) + " objetos de tipo Cursos encontrados en la BD.");
			
			Clases.getClass(db).removeAllInstances();
			Profesores.getClass(db).removeAllInstances();
			Asignaturas.getClass(db).removeAllInstances();
			Cursos.getClass(db).removeAllInstances();

			db.commit();
			db.close();
			System.out.println("\nTodos los objetos de tipo Profesores, Clases, Asignaturas y Cursos eliminados correctamente de la base de datos.");
		} 
		catch (MtException mte) 
		{
			System.out.println("MtException : " + mte.getMessage());
		}
	}

}
