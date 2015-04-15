#version 330

in vec2 texCoords0;

uniform sampler2D diffuse;

out vec4 color;

void main() {
    color = texture2D(diffuse, texCoords0);
}