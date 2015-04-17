#version 330

in vec2 texCoords0;
in vec4 baseColor;

uniform sampler2D diffuse;

out vec4 color;

void main() {
    color = texture2D(diffuse, texCoords0) * baseColor;
}