package modelo;
// Generated 14 ene 2025, 8:59:55 by Hibernate Tools 6.5.1.Final

import java.sql.Date;

/**
 * MatriculacionesId generated by hbm2java
 */
public class MatriculacionesId implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private int alumId;
	private int cicloId;
	private int curso;
	private Date fecha;

	public MatriculacionesId() {
	}

	public MatriculacionesId(int alumId, int cicloId, int curso, Date fecha) {
		this.alumId = alumId;
		this.cicloId = cicloId;
		this.curso = curso;
		this.fecha = fecha;
	}

	public int getAlumId() {
		return this.alumId;
	}

	public void setAlumId(int alumId) {
		this.alumId = alumId;
	}

	public int getCicloId() {
		return this.cicloId;
	}

	public void setCicloId(int cicloId) {
		this.cicloId = cicloId;
	}

	public int getCurso() {
		return this.curso;
	}

	public void setCurso(int curso) {
		this.curso = curso;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof MatriculacionesId))
			return false;
		MatriculacionesId castOther = (MatriculacionesId) other;

		return (this.getAlumId() == castOther.getAlumId()) && (this.getCicloId() == castOther.getCicloId())
				&& (this.getCurso() == castOther.getCurso())
				&& ((this.getFecha() == castOther.getFecha()) || (this.getFecha() != null
						&& castOther.getFecha() != null && this.getFecha().equals(castOther.getFecha())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getAlumId();
		result = 37 * result + this.getCicloId();
		result = 37 * result + this.getCurso();
		result = 37 * result + (getFecha() == null ? 0 : this.getFecha().hashCode());
		return result;
	}

}
