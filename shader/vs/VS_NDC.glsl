#version 430 core

layout (location = 0) in vec3 pos;
layout (location = 1) in vec3 norm;
layout (location = 2) in vec2 tex;
layout (location = 3) in vec3 tan;
layout (location = 4) in vec3 bi_tan;
layout (location = 5) in ivec4 bone_id;
layout (location = 6) in vec4 bone_w;
layout (location = 7) in mat4 instance_m;

out vec2 texture_coord;

void main(){
    texture_coord = tex;
    gl_Position = vec4(pos, 1.0);
}