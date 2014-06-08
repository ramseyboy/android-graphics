attribute vec4 a_Position;
uniform float point_size;

void main() {
    gl_Position = a_Position;
    gl_PointSize = point_size;
}