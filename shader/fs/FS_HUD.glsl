#version 430 core

layout (location = 0) out vec4 fragment_color;

in vec2 texture_coord;

uniform sampler2D image;

void main() {
    fragment_color = texture(image,texture_coord);
}
