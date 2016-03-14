clean:
	-rm -r tmp/*
backup: clean
	tar cvf ../backup.tar .
