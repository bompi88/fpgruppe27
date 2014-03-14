package framework;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import com.google.gson.annotations.Expose;

public abstract class Model {

	@Expose(serialize = false) private PropertyChangeSupport propertyChangeSupport;
	
	public Model() {
		propertyChangeSupport = new PropertyChangeSupport(this);
	}
	
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }
	
//	/**
//	 * Creates a new entry in the sql-database, typically: "INSERT INTO ..."
//	 */
//	public abstract void create() throws ClassNotFoundException, SQLException;
//	
//	/**
//	 * Updates an entry in the sql-database.
//	 */	
//	public abstract void save() throws ClassNotFoundException, SQLException;
//	
//	/**
//	 * Deletes an entry from the sql-database.
//	 */	
//	public abstract void delete() throws ClassNotFoundException, SQLException;
//	
//	/**
//	 * Fetches an entry from the sql-database, typically: "SELECT ..."
//	 * @param id
//	 * @return Model
//	 */	
//	public abstract void fetch() throws ClassNotFoundException, SQLException;
//	
	
}
