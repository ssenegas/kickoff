package com.senegas.kickoff.utils;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class OrthoCamController extends InputAdapter {
	final OrthographicCamera camera;
	final Vector3 curr = new Vector3();
	final Vector3 last = new Vector3(-1, -1, -1);
	final Vector3 delta = new Vector3();

	public OrthoCamController (OrthographicCamera camera) {
		this.camera = camera;
	}

	@Override
	public boolean touchDragged (int x, int y, int pointer) {
        this.camera.unproject(this.curr.set(x, y, 0));
		if (!(this.last.x == -1 && this.last.y == -1 && this.last.z == -1)) {
            this.camera.unproject(this.delta.set(this.last.x, this.last.y, 0));
            this.delta.sub(this.curr);
            this.camera.position.add(this.delta.x, this.delta.y, 0);
		}
        this.last.set(x, y, 0);
		return false;
	}

	@Override
	public boolean touchUp (int x, int y, int pointer, int button) {
        this.last.set(-1, -1, -1);
		return false;
	}
}