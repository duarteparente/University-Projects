#ifdef __APPLE__
#include <GLUT/glut.h>
#else
#include <GL/glut.h>
#endif

#define _USE_MATH_DEFINES
#include <math.h>

GLdouble alpha = 45;
GLdouble beta = 15;
GLdouble r = 10;
float height = 2.0f;
GLenum mode = GL_LINE;


void changeSize(int w, int h) {

	// Prevent a divide by zero, when window is too short
	// (you cant make a window with zero width).
	if(h == 0)
		h = 1;

	// compute window's aspect ratio 
	float ratio = w * 1.0 / h;

	// Set the projection matrix as current
	glMatrixMode(GL_PROJECTION);
	// Load Identity Matrix
	glLoadIdentity();
	
	// Set the viewport to be the entire window
    glViewport(0, 0, w, h);

	// Set perspective
	gluPerspective(45.0f ,ratio, 1.0f ,1000.0f);

	// return to the model view matrix mode
	glMatrixMode(GL_MODELVIEW);
}

void draw_axis(){
	glBegin(GL_LINES);
	// X axis in red
	glColor3f(1.0f, 0.0f, 0.0f);
	glVertex3f(-100.0f, 0.0f, 0.0f);
	glVertex3f( 100.0f, 0.0f, 0.0f);
	// Y Axis in Green
	glColor3f(0.0f, 1.0f, 0.0f);
	glVertex3f(0.0f, -100.0f, 0.0f);
	glVertex3f(0.0f, 100.0f, 0.0f);
	// Z Axis in Blue
	glColor3f(0.0f, 0.0f, 1.0f);
	glVertex3f(0.0f, 0.0f, -100.0f);
	glVertex3f(0.0f, 0.0f, 100.0f);
	glEnd();
}

void drawCylinder(float radius, float heigth, int slices) {
    float angle = 2 * M_PI / slices;
    glBegin(GL_TRIANGLES);
	glColor3f(0.0f, 1.0f, 1.0f);
    for (int i=1; i<=slices; i++){
        
        glVertex3f((radius*sin(angle*i)), heigth/2, radius*cos(angle*i));
        glVertex3f((radius*sin(angle*(i+1))), heigth/2, radius*cos(angle*(i+1)));
        glVertex3f(0.0f, heigth/2, 0.0f);
		
        glVertex3f((radius*sin(angle*(i+1))), -heigth/2, radius*cos(angle*(i+1)));
        glVertex3f((radius*sin(angle*i)), -heigth/2, radius*cos(angle*i));
        glVertex3f(0.0f, -heigth/2, 0.0f);
		
        glVertex3f((radius*sin(angle*i)), heigth/2, radius*cos(angle*i));
        glVertex3f((radius*sin(angle*i)), -heigth/2, radius*cos(angle*i));
        glVertex3f((radius*sin(angle*(i+1))), heigth/2, radius*cos(angle*(i+1)));
		
        glVertex3f((radius*sin(angle*(i+1))), heigth/2, radius*cos(angle*(i+1)));
        glVertex3f((radius*sin(angle*i)), -heigth/2, radius*cos(angle*i));
        glVertex3f((radius*sin(angle*(i+1))), -heigth/2, radius*cos(angle*(i+1)));

    }
    glEnd();
}


void renderScene(void) {

	// clear buffers
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	// set the camera
	glLoadIdentity();
	float px = r*cos(beta)*sin(alpha);
	float py = r*sin(beta);
	float pz = r*cos(beta)*cos(alpha);
	gluLookAt(px, py, pz, 
		      0.0,0.0,0.0,
			  0.0f,1.0f,0.0f);

	draw_axis();
	
	glPolygonMode(GL_FRONT_AND_BACK,mode);

	drawCylinder(1,height,10);

	// End of frame
	glutSwapBuffers();
}


void processKeys(unsigned char key, int xx, int yy) {
	switch(key){
		case '1': mode = GL_FILL; break;
		case '2': mode = GL_LINE; break;
		case '3': mode = GL_POINT; break; 
		case '+': r -= 0.1; break;
		case '-': r += 0.1; break;
	}

	glutPostRedisplay();
}


void processSpecialKeys(int key, int xx, int yy) {
	switch(key){
		case GLUT_KEY_UP: beta += 0.1; break;
		case GLUT_KEY_DOWN: beta -= 0.1; break;
		case GLUT_KEY_RIGHT: alpha += 0.1; break;
		case GLUT_KEY_LEFT: alpha -= 0.1; break;
	}
	glutPostRedisplay();
}


int main(int argc, char **argv) {

// init GLUT and the window
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_DEPTH|GLUT_DOUBLE|GLUT_RGBA);
	glutInitWindowPosition(100,100);
	glutInitWindowSize(800,800);
	glutCreateWindow("CG@DI-UM - Aula3");
		
// Required callback registry 
	glutDisplayFunc(renderScene);
	glutReshapeFunc(changeSize);
	
// Callback registration for keyboard processing
	glutKeyboardFunc(processKeys);
	glutSpecialFunc(processSpecialKeys);

//  OpenGL settings
	glEnable(GL_DEPTH_TEST);
	glEnable(GL_CULL_FACE);
	
// enter GLUT's main cycle
	glutMainLoop();
	
	return 1;
}
