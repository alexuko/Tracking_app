package com.example.tracker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import androidx.annotation.Nullable;

import java.math.RoundingMode;
import java.text.DecimalFormat;


/**
 * GraphView class will extend View class an it is necessary for the creation of customized view
 * this class will create our statistics graph
 * <h5>Global variables</h5>
 *  TAG identifies the class name
 *  repActivity reference to the ReportActivity
 *  paint the paint object.
 *  graphLeftValues the paint object.
 *  graphRightValues the paint object.
 *  graphBottomValues the paint object.
 *  avgSpeedLine the paint object.
 *  avgAltitudeLine the paint object.
 *  locationsLine the paint object.
 *  spaceX space over the X coordinates (left and right side of the screen) .
 *  spaceY space over the Y coordinates (bottom side of the screen) .
 *
 * @author  Alejandro Rivera
 * @version 1.0
 * @since   2019-12-13
 */
public class GraphView extends View {
    private static final String TAG = "GraphView";
    private ReportActivity repActivity = (ReportActivity) this.getContext();
    private Paint paint, graphLeftValues, graphRightValues, graphBottomValues,avgSpeedLine, avgAltitudeLine, locationsLine;
    private int spaceX = 50;
    private int spaceY = 50;

    /**
     * GraphView first constructor will be used when initialising your GraphView entirely
     * through Java code with no XML interaction
     *
     * @param context allows access to application-specific resources and classes
     */
    public GraphView(Context context) {
        super(context);
        init();
    }
    /**
     * The second constructor  form should parse and apply
     * any attributes defined in the layout file.
     * @param context allows access to application-specific resources and classes
     * @param attrs set of attributes
     */
    public GraphView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * GraphView third constructor will be used if the view has been declared in an XML file with a
     * set of attributes and also a style attribute set.
     * @param context allows access to application-specific resources and classes
     * @param attrs set of attributes
     * @param defStyleAttr defined set of attributes
     */
    public GraphView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    /**
     *
     * All three constructors will share the same init initialisation.
     * Here we will initialize the Paint objects assigning them different features
     * as required to each one of them ,
     */
    public void init(){
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.YELLOW);
        paint.setStrokeWidth(5f);

        graphLeftValues = new Paint(Paint.ANTI_ALIAS_FLAG);
        graphLeftValues.setColor(getResources().getColor(R.color.graph_numbers_color));
        graphLeftValues.setTextSize(25f);
        graphLeftValues.setTextAlign(Paint.Align.RIGHT);

        graphRightValues = new Paint(Paint.ANTI_ALIAS_FLAG);
        graphRightValues.setColor(getResources().getColor(R.color.graph_numbers_color));
        graphRightValues.setTextSize(25f);
        graphRightValues.setTextAlign(Paint.Align.LEFT);

        graphBottomValues = new Paint(Paint.ANTI_ALIAS_FLAG);
        graphBottomValues.setColor(getResources().getColor(R.color.graph_numbers_color));
        graphBottomValues.setTextSize(25f);
        graphBottomValues.setTextAlign(Paint.Align.RIGHT);

        avgSpeedLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        avgSpeedLine.setColor(getResources().getColor(R.color.avg_Speed_line_color));

        avgAltitudeLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        avgAltitudeLine.setColor(getResources().getColor(R.color.avg_Alt_line_color));

        locationsLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        locationsLine.setColor(getResources().getColor(R.color.distance_line_color));
        locationsLine.setStrokeWidth(2f);



    }
    /**
     *
     * This method  will draw the contents of our widget Graph using a canvas
     * here we will draw our lines required for drawing a graph, using the following methods
     * setLeftVerticalPoints(canvas);
     * setRightVerticalPoints(canvas);
     * setHorizontalPoints(canvas);
     * drawAverageSpeedLine(canvas);
     * drawAltitudePointsLine(canvas);
     * drawAverageSpeedLine(canvas);
     * drawAverageAltitudeLine(canvas);
     * and the 3 base lines of our graph
     * @param canvas area to draw
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();

        // vertical left line of the graph
        canvas.drawLine(spaceX, 0,spaceX, height - spaceY ,paint);
        // horizontal bottom line of the graph
        canvas.drawLine(spaceX, height - spaceY, width - spaceX , height - spaceY, paint);
        // vertical right line of the graph
        canvas.drawLine(width - spaceX, 0,width - spaceX, height - spaceY ,paint);


        setLeftVerticalPoints(canvas);
        setRightVerticalPoints(canvas);
        setHorizontalPoints(canvas);
        drawAverageSpeedLine(canvas);
        drawAltitudePointsLine(canvas);
        drawAverageSpeedLine(canvas);
        drawAverageAltitudeLine(canvas);
    }

//https://www.freemaptools.com/elevation-finder.htm
//website to get the different altitudes and longitudes

    /**
     *Method will draw a line between all of the different locations stored in the arrayList
     * from point to point starting from the first location, them the endpoint will become the
     * starting points(x and y) all the way until the last one
     * <p>local Variables</p>
     *  endPointX used as end point (margin on the right side) of the graph
     *  endPointY used as bottom end point of the graph
     *  initialPointX used as starting point (margin on the left side) of the graph
     *  valZero fist objects altitude in the arrayList
     *  maxValueAltit gets the max altitude value from the altitudes
     *  realHeight total height minus space on the bottom of the graph
     *  half middle point of the graph
     *  @param canvas area to draw
     */
    private void drawAltitudePointsLine(Canvas canvas){
        float endPointX = spaceX;
        float endPointY = 0;
        //start point X
        float initialPointX = spaceX;
        //fisrt value of the array
        //start point Y
        double valZero = repActivity.getMyPlaces_ra().get(0).getAltitude();
        double maxValueAltit = repActivity.getMaxAltValue();
        double realHeight = getHeight() - spaceY;
        float initialPointY = (float) ((valZero / maxValueAltit) * realHeight);
        float half = ((float) (maxValueAltit / maxValueAltit * realHeight)) / 2;
        initialPointY = MirrorY(initialPointY, half);
        for (int i = 1; i < repActivity.getMyPlaces_ra().size(); i++) {
            //consecutive point
            endPointX += (getWidth() - (spaceX * 2)) / (repActivity.getMyPlaces_ra().size() - 1);
            endPointY = (float) (repActivity.getMyPlaces_ra().get(i).getAltitude() / maxValueAltit * realHeight);
            //endPointY = (float) (avrgalt / maxValueAltit * realHeight);
            endPointY = MirrorY(endPointY, half);
            Log.d(TAG, "endPointX: " + endPointX);
            Log.d(TAG, "endPointY: " + endPointY);
            canvas.drawLine(initialPointX,initialPointY,endPointX,endPointY, locationsLine);
            initialPointX = endPointX;
            initialPointY = endPointY;
        }

        Log.d(TAG, "drawAltitudePointsLine==> " + "\tinitialPointX: " + initialPointX +"\tvalzero: " + valZero + "\tmaxValueAltit: " + maxValueAltit +"\tinitialPoint: " + initialPointY);
    }


    /**
     * Method used to reverse a point on the graph as initially the point appears on the opposite side of it
     * if value is already in the middle of the graph then just returns half otherwise if toReverse is bigger value
     * than half it puts it in the same distance from the half but on the opposite side
     * @param toReverse location value to be relocated
     * @param half middle height  of the graph
     * @return the value reversed
     */
    private float MirrorY(float toReverse, float half) {
        if (toReverse == half)
            return toReverse;
        return ((
                (toReverse > half)
                        ? half - (toReverse - half)
                        : half - toReverse + half)
        );
    }

    /**
     * Method draw the average speed line on the graph, measures the real height,
     * get the max value, average  and the size of the array then (avgspeed/maxspeed) * realheight
     * gives us the coordinate to draw the average speed line
     * @param canvas area to draw
     */
    private void drawAverageSpeedLine(Canvas canvas){
        float realHeight = getHeight() - spaceY;
        double maxSpeed = repActivity.getMaxSpeedValue();
        double speedArrySize = repActivity.getMyPlaces_ra().size();
        double avgSpeed = repActivity.getAvgSpeedValue();
        float avgGraphPoint = (float) ((avgSpeed / maxSpeed) * realHeight);
        Log.d(TAG, "drawAverageSpeedLine --> realHeight: " + realHeight + "\t maxSpeed "+ maxSpeed + "\t speedArrySize "+ speedArrySize+ "\t avgSpeed "+ avgSpeed + "\tavgGraphPoint " + avgGraphPoint);
        canvas.drawLine(spaceX,realHeight - avgGraphPoint,getWidth() - spaceX,realHeight-avgGraphPoint,avgSpeedLine);

    }

    /**
     * Method draw the average Altitude line on the graph, measures the real height,
     * get the max value, average  and the size of the array then (avgAltitude/maxAltitude) * realheight
     * gives us the coordinate to draw the averageAltitude line
     * @param canvas area to draw
     */
    private void drawAverageAltitudeLine(Canvas canvas) {
        float realHeight = getHeight() - spaceY;
        double maxAltitude = repActivity.getMaxAltValue();
        double altitudeArrySize = repActivity.getMyPlaces_ra().size();
        double avgAltitude = repActivity.getAvgAltValue();
        float avgGraphPoint = (float) ((avgAltitude / maxAltitude) * realHeight);
        Log.d(TAG, "drawAverageAltitudeLine --> realHeight: " + realHeight + "\t maxAltitude "+ maxAltitude + "\t altitudeArrySize "+ altitudeArrySize+ "\t avgAltitude "+ avgAltitude + "\tavgGraphPoint " + avgGraphPoint);
        canvas.drawLine(spaceX,realHeight - avgGraphPoint,getWidth() - spaceX,realHeight-avgGraphPoint,avgAltitudeLine);
    }

    /**
     * Method will draw the numerical values on the left hand side of the graph
     * by getting the max altitude value and dividing it by 5 (array of values to hold)
     * then the sum of that division by the previous will give us the 5 numerical values to be displayed on the
     * graph, we take into consideration to draw those values, height and wight for starting placing the numbers
     * evenly only where the left line of the graph is placed,
     * setting a paint = graphLeftValues
     * zero draw at the beginning of the graph
     *
     * @param canvas area to draw
     */
    private void setLeftVerticalPoints(Canvas canvas){
        //max altitude from data
        double altMaxVal = repActivity.getMaxAltValue();
        //max altitude divided by 5
        //int altitDivision = (int) Math.floor(altMaxVal / 5);
        double altitDivision = (altMaxVal / 5);
        //sum of altitudes
        double sumOfAltitudes = 0;
        //size of array
        int arrSize = 5;
        // array for vertical labels
        double[] vertLabels = new double[arrSize];

        // fill up the array vertLabels with values
        for (int i = 0; i < arrSize; i++) {
            vertLabels[i] = altitDivision + sumOfAltitudes;
            sumOfAltitudes += altitDivision;
            Log.d(TAG, "vertLabels: " + i +": "+ vertLabels[i]);
        }


        int startPointVertLine = getHeight() - spaceY;         //681
        // - 5 will push the numbers down 5, so the last number won't be out of bounds
        int heightDivisions = (startPointVertLine / 5) - 5;    //136
        int sumHeightDiv = heightDivisions;
        Log.d(TAG, "getHeight(): "+ getHeight() + "\ngetWidth(): "+ getWidth() + "\nheightDivisions: "+ heightDivisions);
        DecimalFormat df = new DecimalFormat("#");
        df.setRoundingMode(RoundingMode.FLOOR);
        for (int i = 0; i < vertLabels.length ; i++) {
            //each point in the vertical line up by heightDivisions up to 5 times
            canvas.drawText(""+ df.format(vertLabels[i]),spaceX - 10 ,startPointVertLine - sumHeightDiv, graphLeftValues);
            sumHeightDiv += heightDivisions;
        }

    }
    /**
     * Method will draw the numerical values on the right hand side of the graph
     * by getting the max speed value and dividing it by 5 (array of values to hold)
     * then the sum of that division by the previous will give us the 5 numerical values to be displayed on the
     * graph, we take into consideration to draw those values, height and wight for starting placing the numbers
     * evenly only where the right line of the graph is placed, we give a new format as many unnecessary decimal values will appear
     * setting a paint = graphRightValues
     * zero draw at the beginning of the graph
     *
     * @param canvas area to draw
     */
    private void setRightVerticalPoints(Canvas canvas){
        //max altitude from data
        double speedMaxVal = repActivity.getMaxSpeedValue();
        Log.d(TAG, "setRightVerticalPoints + speedMaxVal: "+repActivity.getMaxSpeedValue());
//        double speedDivision = (int) Math.floor(speedMaxVal / 5);
        double speedDivision = (double) speedMaxVal / 5;
        Log.d(TAG, "setRightVerticalPoints + speedDivision: "+repActivity.getMaxSpeedValue());
        //max altitude divided by 5
        //sum of altitudes
        double sumOfSpeeds = 0;
        //size of array
        int arrSize = 5;
        // array for vertical labels
        double[] vertLabels = new double[arrSize];

        //set a new format for the results
        DecimalFormat df = new DecimalFormat("#.#");
        df.setRoundingMode(RoundingMode.FLOOR);
        // fill up the array vertLabels with values
        for (int i = 0; i < arrSize; i++) {
            vertLabels[i] = Double.parseDouble(df.format(speedDivision + sumOfSpeeds));
            sumOfSpeeds += speedDivision;
            Log.d(TAG, "speedLabels: " + i +": "+ vertLabels[i]);
        }


        int startPointVertLine = getHeight() - spaceY;         //681
        // - 5 will push the numbers down 5, so the last number won't be out of bounds
        int heightDivisions = (startPointVertLine / 5) - 5;    //136
        int sumHeightDiv = heightDivisions;
        Log.d(TAG, "getHeight(): "+ getHeight() + "\ngetWidth(): "+ getWidth() + "\nheightDivisions: "+ heightDivisions);

        for (int i = 0; i < vertLabels.length ; i++) {
            //each point in the vertical line up by heightDivisions up to 5 times
            canvas.drawText(""+vertLabels[i],getWidth() - 40,startPointVertLine - sumHeightDiv, graphRightValues);
            sumHeightDiv += heightDivisions;
        }
//
        // zero draw at the beginning of the graph
        canvas.drawText("0",getWidth() - 40,startPointVertLine, graphRightValues);
    }

    /**
     * Method will draw the numerical values on the bottom side of the graph
     * by getting the max time value and dividing it by 5 (array of values to hold)
     * then the sum of that division by the previous will give us the 5 numerical values to be displayed on the
     * graph, we take into consideration to draw those values, height and wight for starting placing the numbers
     * evenly only  where the bottom line of the graph is placed, setting a paint = graphBottomValues
     * zero draw at the beginning of the graph
     *
     * @param canvas area to draw
     */
    private void setHorizontalPoints(Canvas canvas){
        //max time from data
        long maxTimeVal = repActivity.getMaxTime();
        //size of array
        int arrSize = 5;
        //max altitude divided by 5
        long timeDivision = (long) Math.floor(maxTimeVal / arrSize);
        //sum of altitudes
        int sumOftimes = 0;
        // array for vertical labels
        long[] horizontalLabels = new long[arrSize];

        // fill up the array horizontalLabels with values
        for (int i = 0; i < arrSize; i++) {
            horizontalLabels[i] = timeDivision + sumOftimes;
            sumOftimes += timeDivision;
            Log.d(TAG, "Horizontal Labels: " + i +": "+ horizontalLabels[i]);
        }


        int startX = spaceX;         //681
        int startY = getHeight() - 20;         //681
        //widthDivisions  total of the bottom line of the graph (-spaceX * 2 is space left and right side of the graph)
        int widthDivisions = (getWidth() - (spaceX * 2)) / arrSize;    //136
        int sumWidthDiv = widthDivisions;

        for (int i = 0; i < horizontalLabels.length ; i++) {
            //each point in the vertical line up by heightDivisions up to 5 times
            canvas.drawText(""+horizontalLabels[i],startX + sumWidthDiv , startY, graphBottomValues);
            sumWidthDiv += widthDivisions;
        }
        // zero draw at the beginning of the graph
        canvas.drawText("0",startX,startY, graphBottomValues);
    }

}
