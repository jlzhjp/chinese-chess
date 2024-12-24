package io.github.jlzhjp.chess.common;

import javax.swing.event.SwingPropertyChangeSupport;
import java.beans.PropertyChangeListener;

public abstract class Model {
    /**
     * Model 属性更新的事件源
     */
    private final SwingPropertyChangeSupport propertyChangeSupport;

    public Model() {
        propertyChangeSupport = new SwingPropertyChangeSupport(this);
    }

    /**
     * 添加 Model 属性修改的监听器
     *
     * @param listener 要添加的监听器
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    protected void raisePropertyChange(String propName, Object oldValue, Object newValue) {
        // 触发属性更新事件的回调
        propertyChangeSupport.firePropertyChange(propName, oldValue, newValue);
    }
}

