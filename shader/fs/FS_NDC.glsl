#version 430 core

out vec4 fragment_color;

in vec2 texture_coord;

uniform sampler2D main_texture;

uniform sampler2D first_texture;
uniform sampler2D second_texture;

uniform float t1;
uniform float t2;

void main(){

    vec4 firstTexture = texture(first_texture,texture_coord);
    vec4 secondTexture = texture(second_texture,texture_coord);
    vec4 blendColor = (firstTexture + (secondTexture - firstTexture) * t1) * (1.0f - t2);
    vec4 mainTexture = texture(main_texture,texture_coord) * t2;

    fragment_color = blendColor + mainTexture;
}