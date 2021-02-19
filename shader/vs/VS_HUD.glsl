#version 430 core

layout (location = 0) in vec3 pos;
layout (location = 1) in vec2 tex;

layout (row_major, std140) uniform matrices{
    mat4 perspective_m;
    mat4 ortho_m;
    mat4 view_m;
};

out vec2 texture_coord;

uniform mat4 model_m;

void main() {
    gl_Position = ortho_m * model_m * vec4(pos,1.0f);
    texture_coord = tex;
}
