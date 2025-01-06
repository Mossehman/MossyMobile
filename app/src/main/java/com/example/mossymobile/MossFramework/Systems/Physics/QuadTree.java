package com.example.mossymobile.MossFramework.Systems.Physics;

import com.example.mossymobile.MossFramework.Components.Colliders.Collider;
import com.example.mossymobile.MossFramework.Math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class QuadTree {

    private List<Collider> colliders = new ArrayList<>();
    private Rectangle start = null;
    private int cellCapacity = 0;

    private boolean IsDivided = false;

    private QuadTree[] splitNodes = new QuadTree[4];

    public QuadTree(int cellCapacity, Rectangle rect)
    {
        this.start = rect;
        this.cellCapacity = cellCapacity;

        splitNodes[0] = null; //Northeast
        splitNodes[1] = null; //Northwest
        splitNodes[2] = null; //Southeast
        splitNodes[3] = null; //Southwest
    }

    public static class Rectangle {
        Vector2 position = new Vector2();
        Vector2 dimensions = new Vector2();

        public Rectangle(Vector2 position, Vector2 dimensions)
        {
            this.position.x = position.x;
            this.position.y = position.y;

            this.dimensions.x = dimensions.x;
            this.dimensions.y = dimensions.y;
        }


    }

    public void AddCollider(Collider col)
    {
        if (colliders.size() < cellCapacity) {
            colliders.add(col);
        }
        else {
            Subdivide();
            this.IsDivided = true;
        }
    }

    private void Subdivide() {
        splitNodes[0] = new QuadTree(cellCapacity,new Rectangle(new Vector2(start.position.x + start.dimensions.x * 0.5f,
                                                                            start.position.y - start.dimensions.y * 0.5f),
                                                                new Vector2(start.dimensions.x * 0.5f, start.dimensions.y * 0.5f)));

        splitNodes[1] = new QuadTree(cellCapacity,new Rectangle(new Vector2(start.position.x - start.dimensions.x * 0.5f,
                                                                            start.position.y - start.dimensions.y * 0.5f),
                                                                new Vector2(start.dimensions.x * 0.5f, start.dimensions.y * 0.5f)));

        splitNodes[2] = new QuadTree(cellCapacity,new Rectangle(new Vector2(start.position.x + start.dimensions.x * 0.5f,
                                                                            start.position.y + start.dimensions.y * 0.5f),
                                                                new Vector2(start.dimensions.x * 0.5f, start.dimensions.y * 0.5f)));

        splitNodes[3] = new QuadTree(cellCapacity,new Rectangle(new Vector2(start.position.x - start.dimensions.x * 0.5f,
                                                                            start.position.y + start.dimensions.y * 0.5f),
                                                                new Vector2(start.dimensions.x * 0.5f, start.dimensions.y * 0.5f)));
    }

    public Vector2 GetStartPosition()
    {
        return start.position;
    }

    public Vector2 GetDimensions()
    {
        return start.dimensions;
    }
}
