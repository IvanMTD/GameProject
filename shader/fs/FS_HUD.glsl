#version 430 core

#define R 131073
#define G 131074
#define B 131075
#define A 131076

layout (location = 0) out vec4 fragment_color;
layout (location = 1) out vec4 select_color;

in vec2 texture_coord;

uniform sampler2D image;
uniform int group;
uniform float id;

void fragmentColor(vec4);
void selectColor(vec4);

void main() {

    vec4 rgba = texture(image,texture_coord);

    fragmentColor(rgba);
    selectColor(rgba);
}

void fragmentColor(vec4 rgba){
    if(rgba.a == 0.0f){
        discard;
    }
    fragment_color = rgba;
}

void selectColor(vec4 rgba){
    if (group == R){
        select_color = vec4(id, 0.0f, 0.0f, 1.0f);
    } else if (group == G){
        select_color = vec4(0.0f, id, 0.0f, 1.0f);
    } else if (group == B){
        select_color = vec4(0.0f, 0.0f, id, rgba.a);
    } else if (group == A){
        select_color = vec4(0.0f, 0.0f, 0.0f, id);
    }
}
