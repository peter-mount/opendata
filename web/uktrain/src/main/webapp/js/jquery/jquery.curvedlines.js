/* The MIT License

Copyright (c) 2012 Mihir Mone

Code partials from Michael Zinsmaier, nergal.dev, Ernst de Moor

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

/*
options:
_____________________________________________________

active:           bool    true => plugin can be used
show:             bool    true => series will be drawn as curved line
fit:              bool    true => forces the max,mins of the curve to be on the datapoints
lineWidth:        int     width of the line
curvePointFactor  int     defines how many "virtual" points are used per "real" data point to emulate the curvedLines
fitPointDist:     int     defines the x axis distance of the additional two points that are used to enforce the min max condition. 
                          (you will get curvePointFactor * 3 * |datapoints| "virtual" points if fit is true). When using time mode
                          for X axis, this MUST be 0.
fill:             bool    true => area under the lines will be filled
                  int     1 => area under the lines will be filled
fillColor:        string  Named color or HTML HEX code
  
*/

/*
*  Last Updated: 18 October 2012 1515 AEDT
*/

(function ($) {

  var options = {
    series: {
      curvedLines: {
        active: false,
        show: false,
        fit: true,
        lineWidth: 1,
        curvePointFactor: 30,
        fitPointDist: 0,
        fill: false,
        fillColor: null
      }
    }
  };

  function init(plot) {
    var plotOptions = plot.getOptions();
    var isTimeMode = plotOptions.xaxis.mode == "time";

    plot.hooks.processOptions.push(processOptions);

    //if the plugin is active register draw method
    function processOptions(plot, options) {
      if (options.series.curvedLines.active)
        plot.hooks.draw.push(draw);
    }

    //select the data sets that should be drawn with curved lines and draw them     
    function draw(plot, ctx) {
      var series;
      var axes = plot.getAxes();
      var sdata = plot.getData();
      var offset = plot.getPlotOffset();

      for (var i = 0; i < sdata.length; i++) {
        series = sdata[i];
        if (series.curvedLines.show && series.curvedLines.lineWidth > 0) {

          axisx = series.xaxis;
          axisy = series.yaxis;

          ctx.save();
          ctx.translate(offset.left, offset.top);
          ctx.lineJoin = "round";
          ctx.strokeStyle = series.color;
          ctx.lineWidth = series.curvedLines.lineWidth;

          var plotPoints = calculateCurvePoints(series.data, series.curvedLines);

          series.datapoints.points = plotPoints;
          plotLine(ctx, series.datapoints.points, axisx, axisy);

          // check whether to fill the series and compute fill color
          // either via user inputs or use defaults
          if ((typeof series.curvedLines.fill == "number" && series.curvedLines.fill == 1) || (series.curvedLines.fill == true)) {
            var c = series.curvedLines.fillColor ? $.color.parse(series.curvedLines.fillColor) : $.color.parse(series.color);

            if (typeof series.curvedLines.fill == "number")
              c.a = series.curvedLines.fill;
            else if (!series.curvedLines.fillColor)
              c.a = 0.4;

            c.normalize();
            ctx.fillStyle = c.toString();

            plotLineArea(ctx, series.datapoints, axisx, axisy);
          }
          ctx.restore();
        }
      }
    }

    //nearly the same as in the core library
    //only ps is adjusted to 2
    function plotLine(ctx, points, axisx, axisy) {

      var ps = 2;
      var prevx = null;
      var prevy = null;

      ctx.beginPath();

      for (var i = ps; i < points.length; i += ps) {
        var x1 = points[i - ps], y1 = points[i - ps + 1];
        var x2 = points[i], y2 = points[i + 1];

        if (x1 == null || x2 == null)
          continue;

        // clip with ymin
        if (y1 <= y2 && y1 < axisy.min) {
          if (y2 < axisy.min)
            continue;   // line segment is outside
          // compute new intersection point
          x1 = (axisy.min - y1) / (y2 - y1) * (x2 - x1) + x1;
          y1 = axisy.min;
        }
        else if (y2 <= y1 && y2 < axisy.min) {
          if (y1 < axisy.min)
            continue;
          x2 = (axisy.min - y1) / (y2 - y1) * (x2 - x1) + x1;
          y2 = axisy.min;
        }

        // clip with ymax
        if (y1 >= y2 && y1 > axisy.max) {
          if (y2 > axisy.max)
            continue;
          x1 = (axisy.max - y1) / (y2 - y1) * (x2 - x1) + x1;
          y1 = axisy.max;
        }
        else if (y2 >= y1 && y2 > axisy.max) {
          if (y1 > axisy.max)
            continue;
          x2 = (axisy.max - y1) / (y2 - y1) * (x2 - x1) + x1;
          y2 = axisy.max;
        }

        // clip with xmin
        if (x1 <= x2 && x1 < axisx.min) {
          if (x2 < axisx.min)
            continue;
          y1 = (axisx.min - x1) / (x2 - x1) * (y2 - y1) + y1;
          x1 = axisx.min;
        }
        else if (x2 <= x1 && x2 < axisx.min) {
          if (x1 < axisx.min)
            continue;
          y2 = (axisx.min - x1) / (x2 - x1) * (y2 - y1) + y1;
          x2 = axisx.min;
        }

        // clip with xmax
        if (x1 >= x2 && x1 > axisx.max) {
          if (x2 > axisx.max)
            continue;
          y1 = (axisx.max - x1) / (x2 - x1) * (y2 - y1) + y1;
          x1 = axisx.max;
        }
        else if (x2 >= x1 && x2 > axisx.max) {
          if (x1 > axisx.max)
            continue;
          y2 = (axisx.max - x1) / (x2 - x1) * (y2 - y1) + y1;
          x2 = axisx.max;
        }

        if (x1 != prevx || y1 != prevy)
          ctx.moveTo(axisx.p2c(x1), axisy.p2c(y1));

        prevx = x2;
        prevy = y2;
        ctx.lineTo(axisx.p2c(x2), axisy.p2c(y2));
      }
      ctx.stroke();
    }

    // exactly the same as the core library
    function plotLineArea(ctx, datapoints, axisx, axisy) {

      var points = datapoints.points,
                    ps = datapoints.pointsize,
                    bottom = Math.min(Math.max(0, axisy.min), axisy.max),
                    top, lastX = 0, areaOpen = false;

      for (var i = ps; i < points.length; i += ps) {
        var x1 = points[i - ps], y1 = points[i - ps + 1],
                        x2 = points[i], y2 = points[i + 1];

        if (areaOpen && x1 != null && x2 == null) {
          // close area
          ctx.lineTo(axisx.p2c(lastX), axisy.p2c(bottom));
          ctx.fill();
          areaOpen = false;
          continue;
        }

        if (x1 == null || x2 == null)
          continue;

        // clip x values

        // clip with xmin
        if (x1 <= x2 && x1 < axisx.min) {
          if (x2 < axisx.min)
            continue;
          y1 = (axisx.min - x1) / (x2 - x1) * (y2 - y1) + y1;
          x1 = axisx.min;
        }
        else if (x2 <= x1 && x2 < axisx.min) {
          if (x1 < axisx.min)
            continue;
          y2 = (axisx.min - x1) / (x2 - x1) * (y2 - y1) + y1;
          x2 = axisx.min;
        }

        // clip with xmax
        if (x1 >= x2 && x1 > axisx.max) {
          if (x2 > axisx.max)
            continue;
          y1 = (axisx.max - x1) / (x2 - x1) * (y2 - y1) + y1;
          x1 = axisx.max;
        }
        else if (x2 >= x1 && x2 > axisx.max) {
          if (x1 > axisx.max)
            continue;
          y2 = (axisx.max - x1) / (x2 - x1) * (y2 - y1) + y1;
          x2 = axisx.max;
        }

        if (!areaOpen) {
          // open area
          ctx.beginPath();
          ctx.moveTo(axisx.p2c(x1), axisy.p2c(bottom));
          areaOpen = true;
        }

        // now first check the case where both is outside
        if (y1 >= axisy.max && y2 >= axisy.max) {
          ctx.lineTo(axisx.p2c(x1), axisy.p2c(axisy.max));
          ctx.lineTo(axisx.p2c(x2), axisy.p2c(axisy.max));
          lastX = x2;
          continue;
        }
        else if (y1 <= axisy.min && y2 <= axisy.min) {
          ctx.lineTo(axisx.p2c(x1), axisy.p2c(axisy.min));
          ctx.lineTo(axisx.p2c(x2), axisy.p2c(axisy.min));
          lastX = x2;
          continue;
        }

        // else it's a bit more complicated, there might
        // be two rectangles and two triangles we need to fill
        // in; to find these keep track of the current x values
        var x1old = x1, x2old = x2;

        // and clip the y values, without shortcutting

        // clip with ymin
        if (y1 <= y2 && y1 < axisy.min && y2 >= axisy.min) {
          x1 = (axisy.min - y1) / (y2 - y1) * (x2 - x1) + x1;
          y1 = axisy.min;
        }
        else if (y2 <= y1 && y2 < axisy.min && y1 >= axisy.min) {
          x2 = (axisy.min - y1) / (y2 - y1) * (x2 - x1) + x1;
          y2 = axisy.min;
        }

        // clip with ymax
        if (y1 >= y2 && y1 > axisy.max && y2 <= axisy.max) {
          x1 = (axisy.max - y1) / (y2 - y1) * (x2 - x1) + x1;
          y1 = axisy.max;
        }
        else if (y2 >= y1 && y2 > axisy.max && y1 <= axisy.max) {
          x2 = (axisy.max - y1) / (y2 - y1) * (x2 - x1) + x1;
          y2 = axisy.max;
        }


        // if the x value was changed we got a rectangle
        // to fill
        if (x1 != x1old) {
          if (y1 <= axisy.min)
            top = axisy.min;
          else
            top = axisy.max;

          ctx.lineTo(axisx.p2c(x1old), axisy.p2c(top));
          ctx.lineTo(axisx.p2c(x1), axisy.p2c(top));
        }

        // fill the triangles
        ctx.lineTo(axisx.p2c(x1), axisy.p2c(y1));
        ctx.lineTo(axisx.p2c(x2), axisy.p2c(y2));

        // fill the other rectangle if it's there
        if (x2 != x2old) {
          if (y2 <= axisy.min)
            top = axisy.min;
          else
            top = axisy.max;

          ctx.lineTo(axisx.p2c(x2), axisy.p2c(top));
          ctx.lineTo(axisx.p2c(x2old), axisy.p2c(top));
        }

        lastX = Math.max(x2, x2old);
      }

      if (areaOpen) {
        ctx.lineTo(axisx.p2c(lastX), axisy.p2c(bottom));
        ctx.fill();
      }
    }

    function calculateCurvePoints(data, curvedLinesOptions) {

      var num = curvedLinesOptions.curvePointFactor * data.length;
      var xdata = new Array;
      var ydata = new Array;

      if (curvedLinesOptions.fit) {
        //insert a point before and after the "real" data point to force the line
        //to have a max,min at the data point
        var neigh = curvedLinesOptions.fitPointDist;
        var j = 0;

        for (var i = 0; i < data.length; i++) {

          //smooth front
          if (isTimeMode) {
            // patch handle time mode for X axis
            var dt1 = new Date(data[i][0]);
            dt1.setMinutes(dt1.getMinutes() - 1);
            xdata[j] = dt1.getTime();
          }
          else
            xdata[j] = data[i][0] - 0.01;
          if (i > 0) {
            ydata[j] = data[i - 1][1] * neigh + data[i][1] * (1 - neigh);
          } else {
            ydata[j] = data[i][1];
          }
          j++;

          xdata[j] = data[i][0];
          ydata[j] = data[i][1];
          j++;

          //smooth back
          if (isTimeMode) {
            // patch handle time mode for X axis
            var dt1 = new Date(data[i][0]);
            dt1.setMinutes(dt1.getMinutes() + 1);
            xdata[j] = dt1.getTime();
          }
          else
            xdata[j] = data[i][0] + 0.01;
          if ((i + 1) < data.length) {
            ydata[j] = data[i + 1][1] * neigh + data[i][1] * (1 - neigh);
          } else {
            ydata[j] = data[i][1];
          }

          j++;
        }
      } else {
        //just use the datapoints
        for (var i = 0; i < data.length; i++) {
          xdata[i] = data[i][0];
          ydata[i] = data[i][1];
        }
      }

      var n = xdata.length;

      var y2 = new Array();
      var delta = new Array();
      y2[0] = 0;
      y2[n - 1] = 0;
      delta[0] = 0;

      for (var i = 1; i < n - 1; ++i) {
        var d = (xdata[i + 1] - xdata[i - 1]);
        if (d == 0) {
          return null;
        }

        var s = (xdata[i] - xdata[i - 1]) / d;
        var p = s * y2[i - 1] + 2;
        y2[i] = (s - 1) / p;
        delta[i] = (ydata[i + 1] - ydata[i]) / (xdata[i + 1] - xdata[i]) - (ydata[i] - ydata[i - 1]) / (xdata[i] - xdata[i - 1]);
        delta[i] = (6 * delta[i] / (xdata[i + 1] - xdata[i - 1]) - s * delta[i - 1]) / p;
      }

      for (var j = n - 2; j >= 0; --j) {
        y2[j] = y2[j] * y2[j + 1] + delta[j];
      }

      var step = (xdata[n - 1] - xdata[0]) / (num - 1);

      var xnew = new Array;
      var ynew = new Array;
      var result = new Array;

      xnew[0] = xdata[0];
      ynew[0] = ydata[0];


      for (j = 1; j < num; ++j) {
        xnew[j] = xnew[0] + j * step;

        var max = n - 1;
        var min = 0;

        while (max - min > 1) {
          var k = Math.round((max + min) / 2);
          if (xdata[k] > xnew[j]) {
            max = k;
          } else {
            min = k;
          }
        }

        var h = (xdata[max] - xdata[min]);

        if (h == 0) {
          return null;
        }

        var a = (xdata[max] - xnew[j]) / h;
        var b = (xnew[j] - xdata[min]) / h;

        ynew[j] = a * ydata[min] + b * ydata[max] + ((a * a * a - a) * y2[min] + (b * b * b - b) * y2[max]) * (h * h) / 6;
        result.push(xnew[j]);
        result.push(ynew[j]);
      }

      return result;
    }

  } //end init

  $.plot.plugins.push({
    init: init,
    options: options,
    name: 'curvedLines',
    version: '0.2.3'
  });

})(jQuery);
