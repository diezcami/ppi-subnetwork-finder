import math
import sys
import numpy as np
import matplotlib.pyplot as plt


def calculate_specificity(match_filename, go1, go2):
	matches = open(match_filename)
	c1 = open(go1)
	c2 = open(go2)
	categories1 = {}
	categories2 = {}
	allFOs = []
	# Prepare categories 1
	for line in c1:
		cols = line.strip().split('\t')
		if len(cols)>1:
			categories1[cols[0]] = cols[1]
		if cols[1] not in allFOs:
			allFOs.append(cols[1])

	# Prepare categories 2
	for line in c2:
		cols = line.strip().split('\t')
		if len(cols)>1:
			categories2[cols[0]] = cols[1]
		if cols[1] not in allFOs:
			allFOs.append(cols[1])


	# Get the matches
	correct_nodes = 0
	coverage = 0
	total_h = 0.0
	mne = 0
	for line in matches:
		if line.startswith("!"):
			continue

		cols =line.strip().split(" ")
		p1 = cols[0]
		p2 = cols[1]
		if (p1 in categories1) and (p2 in categories2): 
			#aligned pair considered equivalence class if both proteins annotated
			coverage += 1
			if categories1[p1] == categories2[p2]:
				#p1 and p2 are correct nodes
				correct_nodes+=2
			else:
				#for H(C). if both proteins same FO group contribute 0.
				total_h = total_h - (math.log(1/2.0) / math.log(2)) 
		

	try:
		specificity = ((correct_nodes/2) * 100) / float(coverage)
	except ZeroDivisionError:
		specificity = 0.0
	#print "Specificity:", specificity
	try:
		mne = total_h / coverage
	except ZeroDivisionError:
		mne = 0.0
	#print "MNE:", mne
	print correct_nodes, mne, coverage, specificity




if __name__ == "__main__":				
	inputdata = calculate_specificity(sys.argv[1], sys.argv[2], sys.argv[3])	

#print calculate_specificity("spinal_versionI_results_originalnames.txt", "A.fo", "B.fo")


