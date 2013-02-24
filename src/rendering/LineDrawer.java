package rendering;

import java.nio.FloatBuffer;
import java.util.ArrayList;


import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;

import static org.lwjgl.opengl.GL15.*;


import static org.lwjgl.opengl.GL20.*;

public class LineDrawer extends Node {

	private ArrayList<Float> vertices;
	private ArrayList<Float> colors;
	private FloatBuffer internalVertexBuffer;
	private FloatBuffer internalColorBuffer;
	private Buffer vertexBuffer;
	private Buffer colorBuffer;
	private int numVertices;
	private int numColors;
	private boolean needsUpdate;
	private Matrix4f mvp;
	private FloatBuffer mvpBuffer;
	private Shader shader;
	public LineDrawer(int initialSize){
		vertices = new ArrayList<Float>(initialSize*4);
		colors = new ArrayList<Float>(initialSize*6);
		internalVertexBuffer = BufferUtils.createFloatBuffer(initialSize*4);
		internalColorBuffer = BufferUtils.createFloatBuffer(initialSize*6);
		vertexBuffer = new Buffer(GL_ARRAY_BUFFER, GL_DYNAMIC_DRAW, GL_FLOAT, 2);
		colorBuffer = new Buffer(GL_ARRAY_BUFFER, GL_DYNAMIC_DRAW, GL_FLOAT, 3);
		
		for(int i = 0;i<internalVertexBuffer.capacity(); i++){
			
			internalVertexBuffer.put(0.0f);
		}
		
		for(int i = 0; i< internalColorBuffer.capacity(); i++){
			internalColorBuffer.put(0.0f);
		}
		internalVertexBuffer.flip();
		internalColorBuffer.flip();
		vertexBuffer.setData(internalVertexBuffer);
		colorBuffer.setData(internalColorBuffer);
		
		shader = ProgramManager.getShader("lineShader");
		shader.bindAttribute(Attribute.COLOR3D);
		shader.bindAttribute(Attribute.COORD2D);
		shader.bindUniform(Uniform.MVP);
		
		mvp = new Matrix4f();
		mvpBuffer = BufferUtils.createFloatBuffer(16);
		
	
		
	}
	
	public void addLine(float x1,float y1,float x2,float y2,float r,float g,float b){
		vertices.add(numVertices++, x1);
		vertices.add(numVertices++, y1);
		vertices.add(numVertices++, x2);
		vertices.add(numVertices++, y2);
		colors.add(numColors++, r);
		colors.add(numColors++, g);
		colors.add(numColors++, b);
		colors.add(numColors++, r);
		colors.add(numColors++, g);
		colors.add(numColors++, b);
		needsUpdate = true;
	}
	
	private void update()
	{
		needsUpdate = false;
		internalColorBuffer.clear();
		internalVertexBuffer.clear();
		boolean colorResize = false;
		boolean vertexResize = false;
		if(numColors  >= internalColorBuffer.capacity()){
			colorResize = true;
			internalColorBuffer = BufferUtils.createFloatBuffer(numColors);
		}
		for(Float val: colors){
			internalColorBuffer.put(val);
		}
		
		if(numVertices  >= internalVertexBuffer.capacity()){
			vertexResize = true;
			internalColorBuffer = BufferUtils.createFloatBuffer(numVertices);
		}
		
		for(Float val: vertices){
			internalVertexBuffer.put(val);
		}
		internalColorBuffer.flip();
		internalVertexBuffer.flip();
		if(vertexResize)
			vertexBuffer.setData(internalVertexBuffer);
		else
			vertexBuffer.updateData(internalVertexBuffer, 0, numVertices);
		
		if(colorResize)
			colorBuffer.setData(internalColorBuffer);
		else
		 colorBuffer.updateData(internalColorBuffer, 0, numColors);
		
		
	}
	
	public void clear(){
		numVertices = 0;
		numColors = 0;
		needsUpdate = true;
	}
	public void Render(Pipeline pipeline){
		if(needsUpdate)
			update();
		mvpBuffer.clear();
		shader.bind();
		vertexBuffer.bindTo(shader.getAttribute(Attribute.COORD2D));
		colorBuffer.bindTo(shader.getAttribute(Attribute.COLOR3D));
		
		Matrix4f projection = pipeline.getProjectionMatrix();
		Matrix4f modelMatrix = getModelMatrix();
		Matrix4f.mul(projection, modelMatrix, mvp);
		mvp.store(mvpBuffer);
		mvpBuffer.flip();
		//glUniform1f(shader.getUniform(Uniform.DEPTH), getDepth());
		glUniformMatrix4(shader.getUniform(Uniform.MVP), false,mvpBuffer);
		glDrawArrays(GL_LINES, 0, numVertices);
		
		
		
		
	}
	
	
}