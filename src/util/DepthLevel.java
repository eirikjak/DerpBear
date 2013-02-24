package util;

public enum DepthLevel {
	TOP_LVL(0.0f), 
	ACTOR_LVL(0.2f),
	DECAL_LVL(0.4f),
	TILE_LVL(0.6f),
	BACKGROUND_LVL(0.8F);
	
	private float i;
	
	DepthLevel(float i){
		this.i = i;
	}
	
	float getLevel() {
		return i;
	}
}
