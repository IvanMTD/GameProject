#version 430 core

layout (location = 0) in vec2 pos;
layout (location = 1) in vec2 tex;

out vec2 texture_coord;

void main(){
    texture_coord = tex;
    gl_Position = vec4(pos.x, pos.y, 0.0, 1.0);
}