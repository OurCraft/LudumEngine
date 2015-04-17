#version 330

layout(location = 0) in vec3 pos;
layout(location = 1) in vec2 texCoord;
layout(location = 2) in vec4 vertexColor;

out vec2 texCoords0;
out vec4 baseColor;
uniform mat4 transform;
uniform mat4 projection;

void main() {
    texCoords0 = texCoord;
    baseColor = vertexColor;
    gl_Position = projection * transform * vec4(pos, 1);
}