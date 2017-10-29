# PLOTS IN-DEGREE FREQUENCES FOR PEERSIM HOWTO-2
#
set terminal postscript eps
set title ""
set xlabel "in-degree" 
set ylabel "Number of nodes"
#set style fill solid
set logscale y
set logscale x
set xrange [.5:1000]
set yrange [.5:10000]

set title "Topology in-degree distribution (size=10000, alpha=4)"
set output "./indegree-4.eps"
plot "degree-a4.dat" u ($1-1):2 w points notitle

set title "Topology in-degree distribution (size=10000, alpha=20)"
set output "./indegree-20.eps"
plot "degree-a20.dat" u ($1-1):2 w points notitle

set title "Topology in-degree distribution (size=10000, alpha=100)"
set output "./indegree-100.eps"
plot "degree-a100.dat" u ($1-1):2 w points notitle


set terminal x11
