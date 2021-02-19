#version 430 core

out vec4 fragment_color;

in vec2 texture_coord;

uniform sampler2D main_texture;

void main(){
    fragment_color = texture(main_texture,texture_coord);
}