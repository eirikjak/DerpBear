/*
 * This class contains everything that you can add to the physicsworld and render.
 * For objects needing gamelogic see actor.
 * 
 * REMARKS:
 * 
  */

package world.gameobject;

import component.base.Component;
import component.base.PhysicsComponent;
import component.container.ComponentContainer;

public class GameObject {
	
	protected boolean toBeRemoved;
	protected Transform transform;
	protected ComponentContainer componentContainer;
	protected boolean physicsSimulated;
	protected PhysicsComponent physicsComponent;
	
	public GameObject(Transform t,Component ...components){
		toBeRemoved = false;
		physicsSimulated = false;
		this.transform = t;
		componentContainer = new ComponentContainer(); 
		
		for(int i = 0; i<components.length; i++)
			componentContainer.addComponent(components[i]);
	}
	
	public void addComponent(Component component){
		componentContainer.addComponent(component);
	}
	public void move(float dx, float dy) {
		transform.x += dx;
		transform.y += dy;
		if(physicsSimulated) {
			physicsComponent.setPosition(transform.x, transform.y);
		}
	}
	
	public void setPosition(float x, float y) {
		transform.x = x;
		transform.y = y;
		if(physicsSimulated) {
			physicsComponent.setPosition(x, y);
		}
	}
	
	public void rotate(float amount) {
		transform.orientation += amount;
		if(physicsSimulated) {
			physicsComponent.setOrientation(transform.orientation);
		}
	}
	
	public void setOrientation(float orientation) {
		transform.orientation = orientation;
		if(physicsSimulated) {
			physicsComponent.setOrientation(orientation);
		}
	}
	
	public Transform getTransform() {
		return this.transform;
	}
	
	public void update(float dt){
		
		
		for(Component component:componentContainer ){
			component.onUpdate(this, dt);
		}
	}
	
	public boolean isToBeRemoved() {
		return toBeRemoved;
	}
	
	public void setPhysicsSimulated(PhysicsComponent pc) {
		this.physicsSimulated = true;
		this.physicsComponent = pc;
	}
	
	public boolean isPhysicsSimulated() {
		return physicsSimulated; 
	}
	

	
	
}
