package modelo;
// Generated 14 ene 2025, 8:59:55 by Hibernate Tools 6.5.1.Final

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Users generated by hbm2java
 */
public class Users implements Serializable {

	
	private static final long serialVersionUID = 1L;
	private int id;
	private Tipos tipos;
	private String email;
	private String username;
	private String password;
	private String nombre;
	private String apellidos;
	private String dni;
	private String direccion;
	private Integer telefono1;
	private Integer telefono2;
	private byte[] argazkia;
	private Set matriculacioneses = new HashSet(0);
	private Set reunionesesForProfesorId = new HashSet(0);
	private Set reunionesesForAlumnoId = new HashSet(0);
	private Set horarioses = new HashSet(0);

	public Users() {
	}

	public Users(int id, Tipos tipos) {
		this.id = id;
		this.tipos = tipos;
	}

	public Users(int id, Tipos tipos, String email, String username, String password, String nombre, String apellidos,
			String dni, String direccion, Integer telefono1, Integer telefono2, byte[] argazkia, Set matriculacioneses,
			Set reunionesesForProfesorId, Set reunionesesForAlumnoId, Set horarioses) {
		this.id = id;
		this.tipos = tipos;
		this.email = email;
		this.username = username;
		this.password = password;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.dni = dni;
		this.direccion = direccion;
		this.telefono1 = telefono1;
		this.telefono2 = telefono2;
		this.argazkia = argazkia;
		this.matriculacioneses = matriculacioneses;
		this.reunionesesForProfesorId = reunionesesForProfesorId;
		this.reunionesesForAlumnoId = reunionesesForAlumnoId;
		this.horarioses = horarioses;
	}
	public Users(int id, Tipos tipos, String email, String username, String password, String nombre, String apellidos,
			String dni, String direccion, Integer telefono1, Integer telefono2, byte[] argazkia) {
		this.id = id;
		this.tipos = tipos;
		this.email = email;
		this.username = username;
		this.password = password;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.dni = dni;
		this.direccion = direccion;
		this.telefono1 = telefono1;
		this.telefono2 = telefono2;
		this.argazkia = argazkia;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Tipos getTipos() {
		return this.tipos;
	}

	public void setTipos(Tipos tipos) {
		this.tipos = tipos;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return this.apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getDni() {
		return this.dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public Integer getTelefono1() {
		return this.telefono1;
	}

	public void setTelefono1(Integer telefono1) {
		this.telefono1 = telefono1;
	}

	public Integer getTelefono2() {
		return this.telefono2;
	}

	public void setTelefono2(Integer telefono2) {
		this.telefono2 = telefono2;
	}

	public byte[] getArgazkia() {
		return this.argazkia;
	}

	public void setArgazkia(byte[] argazkia) {
		this.argazkia = argazkia;
	}

	public Set getMatriculacioneses() {
		return this.matriculacioneses;
	}

	public void setMatriculacioneses(Set matriculacioneses) {
		this.matriculacioneses = matriculacioneses;
	}

	public Set getReunionesesForProfesorId() {
		return this.reunionesesForProfesorId;
	}

	public void setReunionesesForProfesorId(Set reunionesesForProfesorId) {
		this.reunionesesForProfesorId = reunionesesForProfesorId;
	}

	public Set getReunionesesForAlumnoId() {
		return this.reunionesesForAlumnoId;
	}

	public void setReunionesesForAlumnoId(Set reunionesesForAlumnoId) {
		this.reunionesesForAlumnoId = reunionesesForAlumnoId;
	}

	public Set getHorarioses() {
		return this.horarioses;
	}

	public void setHorarioses(Set horarioses) {
		this.horarioses = horarioses;
	}

	
	
}
