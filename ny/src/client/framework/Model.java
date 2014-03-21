package framework;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import com.google.gson.annotations.Expose;

public class Model {

	@Expose private PropertyChangeSupport propertyChangeSupport;
	
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
	
}