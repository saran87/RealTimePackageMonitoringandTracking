import sys
import urllib2
import json
import datetime
from UnicodeWriter import UnicodeWriter

def createCSV(url):
	content = urllib2.urlopen(url).read()
	objects = json.loads(content)
	for obj in objects:
		if len(obj['value']) > 2 :
			timestamp = datetime.datetime.fromtimestamp(obj['timestamp'] / 1e3)
			with open(str(timestamp) + '.csv', 'wb') as csvfile:
				csvWriter = UnicodeWriter(csvfile);
				x = obj['value']['x'].split(' ')
				y = obj['value']['y'].split(' ')
				z = obj['value']['z'].split(' ')
		
				for i in xrange(0,len(x)):
					row = [x[i],y[i],z[i]]
					csvWriter.writerow(row)
		

if __name__ == "__main__":
	if len(sys.argv) > 1:
   		createCSV(sys.argv[1])
	else:
		print "usage: CSVGenerator.py <url>"
		print "Provide URL to process"
			
