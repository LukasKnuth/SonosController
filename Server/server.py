from flask import Flask
import soco
import json

app = Flask(__name__)

@app.route("/call/received", methods=['POST'])
def call_received():
	masters = get_sonos_masters()
	for player in masters:
		player.pause()
	return json.dumps({'status': 'ok'})

@app.route("/call/ended", methods=['POST'])
def call_ended():
	masters = get_sonos_masters()
	for player in masters:
		player.play()
	return json.dumps({'status': 'ok'})


def get_sonos_masters():
	masters = []
	for zone in soco.discover():
		if zone.is_coordinator:
			masters.append(zone)
	return masters

if __name__ == "__main__":
	# Only run if this is being started from the commandline.
	app.run(host= '0.0.0.0')