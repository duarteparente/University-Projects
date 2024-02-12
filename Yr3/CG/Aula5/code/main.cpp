#include <stdio.h>
#include <time.h>
#include <vector>
#include <iostream>

#ifdef __APPLE__
#include <GLUT/glut.h>
#else
#include <GL/glut.h>
#endif

#define _USE_MATH_DEFINES
#include <math.h>

float alfa = 0.0f, beta = 0.5f, radius = 100.0f;
float alpha_cowboys = 0, alpha_indians = 0;
float camX, camY, camZ;


void spherical2Cartesian() {
	camX = radius * cos(beta) * sin(alfa);
	camY = radius * sin(beta);
	camZ = radius * cos(beta) * cos(alfa);
}

std::vector<float> generate_trees(int n_trees, int r){
	std::vector<float> res;
	time_t t;
	srand((unsigned) time(&t));
	for (int i=0; i<n_trees; i++){
		int x = rand() % 201 + (-100);
		int z = rand() % 201 + (-100);
		while(pow(x,2) + pow(z,2) <= pow(r,2)){
			x = rand() % 201 + (-100);
			z = rand() % 201 + (-100);
		}
		res.push_back(x);
		res.push_back(z);
	}
	return res;
}

void draw_trees(std::vector<float> points){
	for (int i=0; i<points.size(); i+=2){
		glPushMatrix();
		glTranslatef(points[i], 0, points[i+1]);
		glColor3f(0.5,0.2,0);
		glRotatef(-90, 1, 0, 0);
		// TRONCO
		glutSolidCone(0.35, 6, 10, 10);
		glColor3f(0, 0.4, 0);
  		glTranslatef(0, 0, 1);
		// FOLHAS
  		glutSolidCone(1.75, 5, 10, 10);
  		glPopMatrix();
	}
}

void draw_cowboys(int n_cowboys, int r, float alpha){
	glColor3f(0,0,1);
	for (int i=0; i<7; i++){
		glPushMatrix();
		glTranslatef(r*sin(alpha), 1.5, r*cos(alpha));
		glRotatef(-90+(alpha)*(180/M_PI), 0, 1, 0);
		glutSolidTeapot(2);
		glPopMatrix();
		alpha += (2 * M_PI) / n_cowboys;
	}
}

void draw_indians(int n_indians, int r, float alpha){
  	glColor3f(1, 0, 0);
  	for (int i = 0; i < n_indians; i++) {
  	  glPushMatrix();
  	  glTranslatef(r * sin(alpha), 1.5, r* cos(alpha));
  	  glRotatef((alpha / M_PI) * 180, 0, 1, 0);
  	  glutSolidTeapot(2);
  	  glPopMatrix();
  	  alpha += (2 * M_PI) / n_indians;
  	}
}


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



void renderScene(void) {

	// clear buffers
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	// set the camera
	glLoadIdentity();
	gluLookAt(camX, camY, camZ,
		0.0, 0.0, 0.0,
		0.0f, 1.0f, 0.0f);

	glColor3f(0.2f, 0.8f, 0.2f);
	glBegin(GL_TRIANGLES);
		glVertex3f(100.0f, 0, -100.0f);
		glVertex3f(-100.0f, 0, -100.0f);
		glVertex3f(-100.0f, 0, 100.0f);

		glVertex3f(100.0f, 0, -100.0f);
		glVertex3f(-100.0f, 0, 100.0f);
		glVertex3f(100.0f, 0, 100.0f);
	glEnd();
	
	// TORUS
	glPushMatrix();
	glTranslatef(0.0f, 1.0f, 0.0f);
	glColor3f(0.9, 0.0, 0.8);
	glutSolidTorus(1.5,3.5, 20, 20);
	glPopMatrix();
	// TEAPOTS
	draw_cowboys(7,10,alpha_cowboys);
	draw_indians(16,40,alpha_indians);
	alpha_cowboys -= 0.015;
	alpha_indians += 0.02;
	// TREES
	static std::vector<float> trees = generate_trees(1000,50);
	draw_trees(trees);
	glutSwapBuffers();
}


void processKeys(unsigned char c, int xx, int yy) {
	
}


void processSpecialKeys(int key, int xx, int yy) {

	switch (key) {

	case GLUT_KEY_RIGHT:
		alfa -= 0.1; break;

	case GLUT_KEY_LEFT:
		alfa += 0.1; break;

	case GLUT_KEY_UP:
		beta += 0.1f;
		if (beta > 1.5f)
			beta = 1.5f;
		break;

	case GLUT_KEY_DOWN:
		beta -= 0.1f;
		if (beta < -1.5f)
			beta = -1.5f;
		break;

	case GLUT_KEY_PAGE_DOWN: radius -= 2.0f;
		if (radius < 1.0f)
			radius = 1.0f;
		break;

	case GLUT_KEY_PAGE_UP: radius += 2.0f; break;
	}
	spherical2Cartesian();
	glutPostRedisplay();

}


void printInfo() {

	printf("Vendor: %s\n", glGetString(GL_VENDOR));
	printf("Renderer: %s\n", glGetString(GL_RENDERER));
	printf("Version: %s\n", glGetString(GL_VERSION));

	printf("\nUse Arrows to move the camera up/down and left/right\n");
	printf("Home and End control the distance from the camera to the origin");
}


int main(int argc, char **argv) {

// init GLUT and the window
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_DEPTH|GLUT_DOUBLE|GLUT_RGBA);
	glutInitWindowPosition(100,100);
	glutInitWindowSize(1500,900);
	glutCreateWindow("CG@DI-UM");
		
// Required callback registry 
	glutDisplayFunc(renderScene);
	glutReshapeFunc(changeSize);
	glutIdleFunc(renderScene);
	
// Callback registration for keyboard processing
	glutKeyboardFunc(processKeys);
	glutSpecialFunc(processSpecialKeys);

//  OpenGL settings
	glEnable(GL_DEPTH_TEST);
	glEnable(GL_CULL_FACE);

	spherical2Cartesian();

	printInfo();

// enter GLUT's main cycle
	glutMainLoop();
	
	return 1;
}
