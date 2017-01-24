import java.util.ArrayList;

//
//  Clipper.java
//
//Created on June 12, 2016
//
//@author: Srinivas
//
//  Contributor:  Yu-ching Sun 
//

///
// Object for performing clipping
//
///

public class clipper {

    ///
    // clipPolygon
    //
    // Clip the polygon with vertex count in and vertices inx/iny
    // against the rectangular clipping region specified by lower-left corner
    // (llx,lly) and upper-right corner (urx,ury). The resulting vertices are
    // placed in outx/outy.
    //
    // The routine should return the the vertex count of the polygon
    // resulting from the clipping.
    //
    // @param in the number of vertices in the polygon to be clipped
    // @param inx - x coords of vertices of polygon to be clipped.
    // @param iny - y coords of vertices of polygon to be clipped.
    // @param outx - x coords of vertices of polygon resulting after clipping.
    // @param outy - y coords of vertices of polygon resulting after clipping.
    // @param llx - x coord of lower left of clipping rectangle.
    // @param lly - y coord of lower left of clipping rectangle.
    // @param urx - x coord of upper right of clipping rectangle.
    // @param ury - y coord of upper right of clipping rectangle.
    //
    // @return number of vertices in the polygon resulting after clipping
    //
    ///
	public int clipPolygon(int in, float inx[], float iny[],
            float outx[], float outy[], float llx, float lly, float urx, float ury){   
		
			// boundary points
			float maxX = urx;
			float maxY = ury;
			float minX = llx;
			float minY = lly;
			  
			int afterVertices = 0; 
			
			float[] interceptP = new float[2];
			
			boolean insideBoundary = true;
			boolean insideBoundary2 = true;
			boolean insideBoundary3 = true;
			
			ArrayList<Float> holderX = new ArrayList<Float>();
			ArrayList<Float> holderY = new ArrayList<Float>();
			
			// copy inx and iny to holderX and holderY
			for (int i = 0; i < inx.length; i++){
				holderX.add(inx[i]);
				holderY.add(iny[i]);
			}
			

			boolean flag = true;
			
			while( flag ){
				
				flag = false;
				ArrayList<Float> newHolderX = new ArrayList<Float>();
				ArrayList<Float> newHolderY = new ArrayList<Float>();
				
				for (int k = 0; k < holderX.size(); k++){
					
					insideBoundary  = checkInside( holderX.get(k), holderY.get(k), minX, minY, maxX, maxY );
					
					// if vertex is outside
					if (insideBoundary == false){
						
						// if we are not at the first or last vertices
						if ( k != 0 && k != holderX.size()-1){
							
							insideBoundary2  = checkInside( holderX.get(k-1), holderY.get(k-1), minX, minY, maxX, maxY );
							
							// if the current one is outside and the previous one is inside
							if( insideBoundary2 == true ){
								interceptP = intersection( holderX.get(k-1), holderY.get(k-1), 
										holderX.get(k), holderY.get(k),  maxX, maxY, minX,  minY );
								
								newHolderX.add(interceptP[0]);
								newHolderY.add(interceptP[1]);
								flag = true;
							}
							
							insideBoundary3  = checkInside( holderX.get(k+1), holderY.get(k+1), minX, minY, maxX, maxY );
							
							// if the current one is outside and the next one is inside
							if( insideBoundary3 == true ){
								interceptP = intersection(holderX.get(k+1), holderY.get(k+1), holderX.get(k), holderY.get(k),  maxX, maxY,
							               minX,  minY);
								
								newHolderX.add(interceptP[0]);
								newHolderY.add(interceptP[1]);
								flag = true;
							}							
						}// end for not the first vertex
						
						// the first vertex
						if ( k == 0 ){
							
							insideBoundary2  = checkInside( holderX.get(holderX.size()-1), holderY.get(holderX.size()-1), 
									                       minX, minY, maxX, maxY );
							
							// if the current one is outside and the previous one is inside
							if( insideBoundary2 == true ){
								interceptP = intersection(holderX.get(holderX.size()-1), holderY.get(holderX.size()-1), 
										holderX.get(k), holderY.get(k),  maxX, maxY, minX,  minY);
								
								newHolderX.add(interceptP[0]);
								newHolderY.add(interceptP[1]);
								flag = true;
							}
							
							insideBoundary3  = checkInside( holderX.get(k+1), holderY.get(k+1), minX, minY, maxX, maxY );
							
							// if the current one is outside and the next one is inside
							if( insideBoundary3 == true ){
								interceptP = intersection(holderX.get(k+1), holderY.get(k+1), holderX.get(k),
										holderY.get(k),  maxX, maxY, minX,  minY);
								
								newHolderX.add(interceptP[0]);
								newHolderY.add(interceptP[1]);
								flag = true;								
							}							
						}// end for the first vertex
						
						// the last vertex
						if ( k == holderX.size()-1 ){
							
							insideBoundary2  = checkInside( holderX.get(k-1), holderY.get(k-1), minX, minY, maxX, maxY );
							
							// if the current one is outside and the previous one is inside
							if( insideBoundary2 == true ){
								interceptP = intersection(holderX.get(k-1), holderY.get(k-1), holderX.get(k), holderY.get(k),  maxX, maxY,
							               minX,  minY);
								
								newHolderX.add(interceptP[0]);
								newHolderY.add(interceptP[1]);
								flag = true;
							}
							
							insideBoundary3  = checkInside( holderX.get(0), holderY.get(0), minX, minY, maxX, maxY );
							
							// if the current one is outside and the next one is inside
							if( insideBoundary3 == true ){
								interceptP = intersection(holderX.get(0), holderY.get(0), holderX.get(k), holderY.get(k),  maxX, maxY,
							               minX,  minY);
								
								newHolderX.add(interceptP[0]);
								newHolderY.add(interceptP[1]);
								flag = true;
							}														
						}// end for the last vertex
						
						// if previous and next vertices are outside
						if( insideBoundary2 == false && insideBoundary3 == false ){
							
							// adjust the coordination according to the clipping edge
							if ( holderX.get(k) > maxX ){
								newHolderX.add(maxX);
								newHolderY.add(holderY.get(k));
							}
							else if( holderY.get(k) > maxY){
								newHolderX.add(holderX.get(k));
								newHolderY.add(maxY);
							}
							else if( holderX.get(k) < minX ){
								newHolderX.add(minX);
								newHolderY.add(holderY.get(k));
							}
							else if( holderY.get(k) < minY){
								newHolderX.add(holderX.get(k));
								newHolderY.add(minY);
							}
						}						
					}// end for the vertex is outside
					
					// the vertex is inside
					else{
						newHolderX.add(holderX.get(k));
						newHolderY.add(holderY.get(k));						
					}					
				}// end of for loop
				
				holderX = newHolderX;
				holderY = newHolderY;
			
			}// end of while loop
			
			// copy holder x, y to the outx and outy		
			for(int g = 0; g < holderX.size(); g++ ){
				outx[g] = holderX.get(g);
				outy[g] = holderY.get(g); 
			}
		
			afterVertices = holderX.size();
			return afterVertices; // should return number of vertices in clipped poly.
    }


    /**
     * Find the intersection of two lines
     * 
     * @param previousX: the inside vertex's x coordinate
     * @param previousY: the inside vertex's y coordinate
     * @param currentX: the outside vertex's x coordinate
     * @param currentY: the outside vertex's y coordinate
     * @param maxX: maximum clipping x
     * @param maxY: maximum clipping y
     * @param minX: minimum clipping x
     * @param minY: minimum clipping y
     * @return intersectP : intersection points 
     */
	
    public float[] intersection( float previousX, float previousY, float currentX, float currentY , float maxX, float maxY,
    		                     float minX, float minY){
    	
    	 float[] intersectP = new float[2];
    	 float deltaX = currentX - previousX ;
    	 float deltaY = currentY - previousY;
    	 float slope = deltaY / deltaX;
    	 float e = 0; 
    	 float d = 0; 
    	 
    	 // right clipping boundary
    	 if ( currentX > maxX ){
    		 e = currentX - maxX;
    		 d = e*(slope);
    		 intersectP[0] = maxX;
    		 
    		 if ( currentY > previousY ){
    			 intersectP[1] = currentY - d;
    		 }
    		 else if( currentY < previousY){
    			 intersectP[1] = currentY + d;
    		 }
    		 else{
    			 intersectP[1 ] = currentY;
    		 }	 
    	 }
    	 
    	 // top 
    	 else if( currentY > maxY){
    		 
    		 e = d*(1/slope);
    		 d = currentY - maxY;
    		 
    		 // outside vertex x > inside vertex x 
    		 if ( currentX > previousX ){
    			 intersectP[0] = currentX - e;
    		 }
    		 else if ( currentX < previousX ){
    			 intersectP[0] = currentX + e;
    		 }
    		 else{
    			 intersectP[0] = currentX;
    		 }
    		 intersectP[1] = maxY;
    	 }
    	 
    	 // left 
    	 else if( currentX < minX ){
    		 e =  minX - currentX ;
    		 d = e*(slope);
    		    		 
    		 if ( currentY > previousY ){
    			 intersectP[1] = currentY - d;
    		 }
    		 else if( currentY < previousY){
    			 intersectP[1] = currentY + d;
    		 }
    		 else{
    			 intersectP[1] = currentY;
    		 }	 
    		 intersectP[0] = minX;
    	 }
    	 
    	 // bottom
    	 else if( currentY < minY ){
    		 d = minY - currentY;
    		 e = Math.abs(d*(1/slope));
    		 if ( currentX > previousX ){
    			 intersectP[0] = previousX + e;
    			 
    		 }
    		 else if( currentX < previousX ){
    			 intersectP[0] = previousX - e;
    		 }
    		 else{
    			 intersectP[0]= previousX;
    		 }
    		 intersectP[1] = minY;
    	 }
    	 
    	 return intersectP;     	 
    }
    
    /**
     * Check if the vertex is inside or outside the clipping area
     * 
     * @param x: the vertex's x coordinate
     * @param y: the vertex's y coordinate
     * @param llx: lower left x
     * @param lly: lower left y
     * @param urx: upper right x
     * @param ury: upper right y
     * @return true if the vertex is inside the clipping area, false if outside the clipping area
     */
    
    public boolean checkInside( float x, float y , float llx, float lly, float urx, float ury ){
    	
   	 float maxX = urx;     	 
   	 float maxY= ury;
   	 float minX = llx;
   	 float minY = lly;
   	 
   	 float[] clipPointsX = new float[4];
     float[] clipPointsY = new float[4];
       
   	 // start from right
     clipPointsX[0] = urx;
     clipPointsY[0] = lly;
     clipPointsX[1] = urx;
     clipPointsY[1] = ury;
     clipPointsX[2] = llx;
     clipPointsY[2] = ury;
     clipPointsX[3] = llx;
     clipPointsY[3] = lly;
   	 
   	 int index1 = 0;
   	 int index2 = 0; 
   	 
   	 // check if the vertex against 4 edges of clipping area
   	 for ( int i = 0; i< clipPointsX.length; i++){
   		 if ( i == clipPointsX.length-1 ){
      		 index1 = i; 
      		 index2 = 0;
   		 }
   		 else{
   			 index1 = i;
   			 index2 = i+1;
   		 }
   		 
   		 // vertical line
   		 if( clipPointsX[index1] - clipPointsX[index2] == 0 ){
   			 
   			 // right boundary
   			 if ( clipPointsX[index2] == maxX){
   				 
   				 // x is on the right of the right edge
      			 if ( x > clipPointsX[index1]){
      				 return false;
   			     }      			 
   			 }
   			 else if( clipPointsX[index2] == minX ){
   				 
   				 // left boundary
   				 if ( x < clipPointsX[index1] ){
   					 return false;
   				 }   			
   			 }   			 
   		 }
   		 
   		 // horizontal line
   		 else if( clipPointsY[index1] - clipPointsY[index2] == 0){
   			 
   			 // top boundary
   			 if ( clipPointsY[index2] == maxY ){
   			 
      			 if ( y > clipPointsY[index1]){
      				 return false;
      			 }
      			
   			 }
   			 
   			 // bottom boundary
   			 else if( clipPointsY[index2] == minY ){
   				
   				 if ( y < clipPointsY[index1]){
      				 return false;
      			 }   				 
   			 }   			 
   		 }  		     		
   	 }
   	return true;    	 
   }   
}