import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.util.ArrayList;
import java.util.List;

public class ShaderUtils {
	
	private ShaderUtils(){}
	
	public static class ProgramBuilder {
		
		private final int program;
		private final List<Integer> shaders;
		
		private ProgramBuilder(int program) {
			this.program = program;
			shaders = new ArrayList<>();
		}
		
		public ProgramBuilder attachShader(int shader) {
			glAttachShader(program, shader);
			shaders.add(shader);
			return this;
		}
		
		public int build() {
			glLinkProgram(program);
			int isLinked = glGetProgrami(program, GL_LINK_STATUS);
			if (isLinked == GL_FALSE) {
				String log = glGetProgramInfoLog(program);
				glDeleteProgram(program);
				// HANDLE THIS BETTER
				throw new RuntimeException(log + "\nFailed to build ShaderProgram");
			}
			for (Integer s : shaders) {
				glDetachShader(program, s);
			}
			shaders.clear();
			return program;
		}
	}
	
	public static int compileShader(int type, String src) {
		int shader = glCreateShader(type);
		// ERROR CHECK HERE
		glShaderSource(shader, src);
		glCompileShader(shader);
		int isCompiled = glGetShaderi(shader, GL_COMPILE_STATUS);
		if (isCompiled == GL_FALSE) {
			String log = glGetShaderInfoLog(shader);
			glDeleteShader(shader);;
			throw new RuntimeException(log + "\nFailed to compiler Shader");
		}
		return shader;
	}
	
	public static ProgramBuilder createProgram() {
		int program = glCreateProgram();
		// ERROR CHECK HERE
		return new ProgramBuilder(program);
	}
}
