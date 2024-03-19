package com.ca.mfd.prc.pqs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.poi.hpsf.Decimal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Schema(description = "控制图对象")
public class ControlChartsConstantsDto {
    private String featureName;

    public static Map<String,Double>  result(Integer testN)
    {

        Map<Integer,Map<String,Double>> resultMap = new HashMap<Integer,Map<String,Double>>();

        String[] column ={"n",  "A",   "A2",  "A3",  "B3", "B4",  "B5",  "B6",  "D1",  "D2",  "D3",  "D4",  "c4",   "1/c4", "d2",  "1/d2"};

        double n[][] ={

                {2,2.121, 1.880F, 2.659F, 0.000F, 3.267, 0.000, 2.606, 0.000, 3.686, 0.000, 3.267, 0.7979, 1.2533, 1.128, 0.8865},
                {3,   1.732, 1.023, 1.954, 0.000, 2.568, 0.000, 2.276, 0.000, 4.358, 0.000, 2.574, 0.8862, 1.1284, 1.693, 0.5907},
     {4,   1.500, 0.729, 1.628, 0.000, 2.266, 0.000, 2.088, 0.000, 4.698, 0.000, 2.282, 0.9213, 1.0854, 2.059, 0.4857},
     {5,   1.342, 0.577, 1.427, 0.000, 2.089, 0.000, 1.964, 0.000, 4.918, 0.000, 2.114, 0.9400, 1.0638, 2.326, 0.4299},
     {6,   1.225, 0.483, 1.287, 0.030, 1.970, 0.029, 1874, 0.000, 5.078, 0.000, 2.004, 0.9515, 1.0510, 2.534, 0.3946},
     {7,   1.134, 0.419, 1.182, 0.118, 1.882, 0.113, 1.806, 0.204, 5.204, 0.076, 1.924, 0.9594, 1.0423, 2.704, 0.3698},
     {8,   1.061, 0.373, 1.099, 0.185, 1.815, 0.179, 1.751, 0.388, 5.306, 0.136, 1.864, 0.9650, 1.0363, 2.847, 0.3512},
     {9,   1.000, 0.337, 1.032, 0.239, 1.761, 0.232, 1.707, 0.547, 5.393, 0.184, 1.816, 0.9693, 1.0317, 2.970, 0.3367},
     {10,  0.949, 0.308, 0.975, 0.284, 1.716, 0.276, 1.669, 0.687, 5.469, 0.223, 1.777, 0.9727, 1.0281, 3.078, 0.3249},
     {11,  0.905, 0.285, 0.927, 0.321, 1.679, 0.313, 1.637, 0.811, 5.535, 0.256, 1.744, 0.9754, 1.0252, 3.173, 0.3152},
     {12,  0.866, 0.266, 0.886, 0.354, 1.646, 0.346, 1.610, 0.922, 5.594, 0.283, 1.717, 0.9776, 1.0229, 3.258, 0.3069},
     {13,  0.832, 0.249, 0.850, 0.382, 1.618, 0.374, 1.585, 1.025, 5.647, 0.307, 1.693, 0.9794, 1.0210, 3.336, 0.2998},
     {14,  0.802, 0.235, 0.817, 0.406, 1.594, 0.399, 1.563, 1.118, 5.696, 0.328, 1.672, 0.9810, 1.0194, 3.407, 0.2935},
     {15,  0.775, 0.223, 0.789, 0.428, 1.572, 0.421, 1.544, 1.203, 5.741, 0.347, 1.653, 0.9823, 1.0180, 3.472, 0.2880},
     {16,  0.750, 0.212, 0.763, 0.448, 1.552, 0.440, 1.526, 1.282, 5.782, 0.363, 1.637, 0.9835, 1.0168, 3.532, 0.2831},
     {17,  0.728, 0.203, 0.739, 0.466, 1.534, 0.458, 1.511, 1.356, 5.820, 0.378, 1.622, 0.9845, 1.0157, 3.588, 0.2787},
     {18,  0.707, 0.194, 0.718, 0.482, 1.518, 0.475, 1.496, 1.424, 5.856, 0.391, 1.608, 0.9854, 1.0148, 3.640, 0.2747},
     {19,  0.688, 0.187, 0.698, 0.497, 1.503, 0.490, 1.483, 1.487, 5.891, 0.403, 1.597, 0.9862, 1.0140, 3.689, 0.2711},
     {20,  0.671, 0.180, 0.680, 0.510, 1.490, 0.504, 1.470, 1.549, 5.921, 0.415, 1.585, 0.9869, 1.0133, 3.735, 0.2677},
     {21,  0.655, 0.173, 0.663, 0.523, 1.477, 0.516, 1.459, 1.605, 5.951, 0.425, 1.575, 0.9876, 1.0216, 3.778, 0.2647},
     {22,  0.640, 0.167, 0.647, 0.534, 1.466, 0.528, 1.448, 1.659, 5.979, 0.434, 1.566, 0.9882, 1.0119, 3.819, 0.2618},
     {23,  0.626, 0.162, 0.633, 0.545, 1.455, 0.539, 1.438, 1.710, 6.006, 0.443, 1.557, 0.9887, 1.0114, 3.858, 0.2592},
     {24,  0.612, 0.157, 0.619, 0.555, 1.445, 0.549, 1.429, 1.759, 6.031, 0.451, 1.548, 0.9892, 1.0109, 3.895, 0.2567},
     {25,  0.600, 0.153, 0.606, 0.565, 1.435, 0.559, 1.420, 1.806, 6.056, 0.459, 1.541, 0.9896, 1.0105, 3.931, 0.2544}
        };

        for (double [] outer:n)
        {
            HashMap<String,Double> tempHashMap =  new HashMap<String,Double>();
           for(int i=0;i<column.length;i++)
           {
              String key =   column[i];
              Double value =outer[i];
              tempHashMap.put(key,value);
           }
            resultMap.put( (int) outer[0],tempHashMap);
        }
       return resultMap.get(testN);
    };



}
