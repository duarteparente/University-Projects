
#include<iostream>
#include<stdio.h>
#include<stdlib.h>

#define _USE_MATH_DEFINES
#include <math.h>
#include <vector>

#include <IL/il.h>

#ifdef __APPLE__
#include <GLUT/glut.h>
#else
#include <GL/glew.h>
#include <GL/glut.h>
#endif

using namespace std;

GLuint vertices[1];

unsigned int t, tw, th;
unsigned char *imageData;

float camX = 00, camY = 30, camZ = 40;
int startX, startY, tracking = 0;

float alpha_cowboys = 0, alpha_indians = 0;
int alpha = 0, beta = 45, r = 50;

void spherical2Cartesian() {
	camX = r * cos(beta) * sin(alpha);
	camY = r * sin(beta);
	camZ = r * cos(beta) * cos(alpha);
}


void changeSize(int w, int h) {

	// Prevent a divide by zero, when window is too short
	// (you cant make a window with zero width).
	if(h == 0)
		h = 1;

	// compute window's aspect ratio 
	float ratio = w * 1.0 / h;

	// Reset the coordinate system before modifying
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	
	// Set the viewport to be the entire window
    glViewport(0, 0, w, h);

	// Set the correct perspective
	gluPerspective(45,ratio,1,1000);

	// return to the model view matrix mode
	glMatrixMode(GL_MODELVIEW);
}


vector<float> generate_trees(int n_trees, int r){
	std::vector<float> res;
	time_t t;
	srand((unsigned) time(&t));
	for (int i=0; i<n_trees; i++){
		int x = rand() % 257 + (-128);
		int z = rand() % 257 + (-128);
		while(pow(x,2) + pow(z,2) <= pow(r,2)){
			x = rand() % 257 + (-128);
			z = rand() % 257 + (-128);
		}
		res.push_back(x);
		res.push_back(z);
	}
	return res;
}

void draw_trees(std::vector<float> points){
	glPolygonMode(GL_FRONT, GL_FILL);
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




void drawTerrain() {

    // colocar aqui o código de desnho do terreno usando VBOs com TRIANGLE_STRIPS
	glBindBuffer(GL_ARRAY_BUFFER, vertices[0]);
	glVertexPointer(3, GL_FLOAT, 0, 0);
	for (int i = 0; i < th - 1 ; i++) {
		glDrawArrays(GL_TRIANGLE_STRIP, tw * 2 * i, tw * 2);
	}
}



void renderScene(void) {

	float pos[4] = {-1.0, 1.0, 1.0, 0.0};

	glClearColor(0.0f,0.0f,0.0f,0.0f);
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	glLoadIdentity();
	gluLookAt(camX, camY, camZ, 
		      0.0,0.0,0.0,
			  0.0f,1.0f,0.0f);

	glPolygonMode(GL_FRONT, GL_LINE);
	glColor3f(0, 0.4, 0);
	drawTerrain();

	// TORUS
	//glPushMatrix();
	//glTranslatef(0.0f, 1.0f, 0.0f);
	//glColor3f(0.9, 0.0, 0.8);
	//glutSolidTorus(1.5,3.5, 20, 20);
	//glPopMatrix();
	//// TEAPOTS
	//draw_cowboys(7,10,alpha_cowboys);
	//draw_indians(16,40,alpha_indians);
	//alpha_cowboys -= 0.015;
	//alpha_indians += 0.02;
	//// TREES
	//static std::vector<float> trees = generate_trees(1500,50);
	//draw_trees(trees);


// End of frame
	glutSwapBuffers();
}



void processKeys(unsigned char key, int xx, int yy) {

// put code to process regular keys in here
}

void processSpecialKeys(int key, int xx, int yy) {

	switch (key) {


	case GLUT_KEY_PAGE_DOWN: r -= 2.0f;
		if (r < 1.0f)
			r = 1.0f;
		break;

	case GLUT_KEY_PAGE_UP: r += 2.0f; break;
	}
	spherical2Cartesian();
	glutPostRedisplay();

}



void processMouseButtons(int button, int state, int xx, int yy) {
	
	if (state == GLUT_DOWN)  {
		startX = xx;
		startY = yy;
		if (button == GLUT_LEFT_BUTTON)
			tracking = 1;
		else if (button == GLUT_RIGHT_BUTTON)
			tracking = 2;
		else
			tracking = 0;
	}
	else if (state == GLUT_UP) {
		if (tracking == 1) {
			alpha += (xx - startX);
			beta += (yy - startY);
		}
		else if (tracking == 2) {
			
			r -= yy - startY;
			if (r < 3)
				r = 3.0;
		}
		tracking = 0;
	}
}


void processMouseMotion(int xx, int yy) {

	int deltaX, deltaY;
	int alphaAux, betaAux;
	int rAux;

	if (!tracking)
		return;

	deltaX = xx - startX;
	deltaY = yy - startY;

	if (tracking == 1) {


		alphaAux = alpha + deltaX;
		betaAux = beta + deltaY;

		if (betaAux > 85.0)
			betaAux = 85.0;
		else if (betaAux < -85.0)
			betaAux = -85.0;

		rAux = r;
	}
	else if (tracking == 2) {

		alphaAux = alpha;
		betaAux = beta;
		rAux = r - deltaY;
		if (rAux < 3)
			rAux = 3;
	}
	camX = rAux * sin(alphaAux * 3.14 / 180.0) * cos(betaAux * 3.14 / 180.0);
	camZ = rAux * cos(alphaAux * 3.14 / 180.0) * cos(betaAux * 3.14 / 180.0);
	camY = rAux * 							     sin(betaAux * 3.14 / 180.0);
}


float height(int i, int j){
	return imageData[i*th + j] * (50.0 / 255.0);
	
}

vector<float> gen_height(){
	vector<float> points; 
    for (int h = 0; h < th; h++) {
        for (int  w = 0; w < tw; w++) {
			points.push_back(w-(tw/2.0)); 
			points.push_back(height(h,w));
			points.push_back(h - (th/2.0));
			points.push_back(w-(tw/2.0)); 
			points.push_back(height(h+1,w));
			points.push_back(h+1 - (th/2.0));
        }
    }
	return points;
}


void init() {

// 	Load the height map "terreno.jpg"

    ilGenImages(1, &t);
    ilBindImage(t);
    // terreno.jpg is the image containing our height map
    ilLoadImage((ILstring) "terreno.jpg");
    // convert the image to single channel per pixel
    // with values ranging between 0 and 255
    ilConvertImage(IL_LUMINANCE, IL_UNSIGNED_BYTE);
    // important: check tw and th values
    // both should be equal to 256
    // if not there was an error loading the image
    // most likely the image could not be found
    tw = ilGetInteger(IL_IMAGE_WIDTH);
    th = ilGetInteger(IL_IMAGE_HEIGHT);
    // imageData is a LINEAR array with the pixel values
    imageData = ilGetData();

// 	Build the vertex arrays
	vector<float> points = gen_height();
	glGenBuffers(1, vertices);
	glBindBuffer(GL_ARRAY_BUFFER, vertices[0]);
	glBufferData( GL_ARRAY_BUFFER, // tipo do buffer, só é relevante na altura do desenho
				  sizeof(float) * points.size(), // tamanho do vector em bytes
				  points.data(), // os dados do array associado ao vector
				  GL_STATIC_DRAW); // indicativo da utilização (estático e para desenho)

// 	OpenGL settings
	glEnable(GL_DEPTH_TEST);
	//glEnable(GL_CULL_FACE);
}


int main(int argc, char **argv) {

// init GLUT and the window
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_DEPTH|GLUT_DOUBLE|GLUT_RGBA);
	glutInitWindowPosition(100,100);
	glutInitWindowSize(800,800);
	glutCreateWindow("CG@DI-UM");

	glEnableClientState(GL_VERTEX_ARRAY);
	glPolygonMode(GL_FRONT, GL_LINE);
		

// Required callback registry 
	glutDisplayFunc(renderScene);
	glutIdleFunc(renderScene);
	glutReshapeFunc(changeSize);

// Callback registration for keyboard processing
	glutKeyboardFunc(processKeys);
	glutSpecialFunc(processSpecialKeys);
	glutMouseFunc(processMouseButtons);
	glutMotionFunc(processMouseMotion);

	glewInit();
	ilInit();
	init();	

// enter GLUT's main cycle
	glutMainLoop();
	
	return 0;
}

