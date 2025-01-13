package com.example.mossymobile.MossFramework.Systems.Physics;

import android.graphics.Color;
import android.util.Log;

import com.example.mossymobile.MossFramework.Components.Colliders.Collider;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.Systems.Debugging.Gizmos;

import java.util.ArrayList;
import java.util.List;

public class QuadTree {
    public final int MaxDepth = 5;
    public int CurrDepth = 0;
    private List<Collider> colliders = new ArrayList<>();
    private Rectangle QuadTreeBoundary = null;
    private int cellCapacity = 0;

    private boolean IsDivided = false;

    private QuadTree[] splitNodes = new QuadTree[4];

    public QuadTree(int cellCapacity, Rectangle rect)
    {
        this.QuadTreeBoundary = rect;
        this.cellCapacity = cellCapacity;

        splitNodes[0] = null; //Northeast (Top right)
        splitNodes[1] = null; //Northwest (Top left)
        splitNodes[2] = null; //Southeast (Bottom right)
        splitNodes[3] = null; //Southwest (Bottom left)
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
        //collider already exists in this node, do not add
        if (colliders.contains(col)) { return; }

        //get the bounding box of the collider to add
        Vector2 bounds = col.GetBounds();
        Vector2 pos = col.GetGameObject().GetTransform().GetPosition();
        pos.Add(col.Offset);

        //check if the collider intersects with the bounding box of the tree node
        if (!Intersects(pos, bounds)) { return; }

        //if there is an intersection, add the collider to the list
        colliders.add(col);

        //if there is no more room in the cell, we split it into 4 leaf nodes
        if (colliders.size() > cellCapacity) {
            colliders.add(col);     //add new collider to list of colliders (this is to avoid creating a new list)
            Subdivide(col);   //subdivide this node into its leaf nodes
        }
    }

    //simple AABB to check if the collider intersects with this rectangle
    public boolean Intersects(Vector2 pos, Vector2 bounds)
    {
        boolean xOverlap = Math.abs(pos.x - QuadTreeBoundary.position.x) <= (bounds.x + QuadTreeBoundary.dimensions.x);
        boolean yOverlap = Math.abs(pos.y - QuadTreeBoundary.position.y) <= (bounds.y + QuadTreeBoundary.dimensions.y);

        return xOverlap && yOverlap;
    }

    private void Subdivide(Collider col) {
        //if we have already divided the tree, do not split it again (this will reset the child nodes)
        if (!IsDivided && CurrDepth < MaxDepth) {
            splitNodes[0] = new QuadTree(cellCapacity, new Rectangle(new Vector2(QuadTreeBoundary.position.x + QuadTreeBoundary.dimensions.x * 0.5f,
                    QuadTreeBoundary.position.y - QuadTreeBoundary.dimensions.y * 0.5f),
                    new Vector2(QuadTreeBoundary.dimensions.x * 0.5f, QuadTreeBoundary.dimensions.y * 0.5f)));

            splitNodes[1] = new QuadTree(cellCapacity, new Rectangle(new Vector2(QuadTreeBoundary.position.x - QuadTreeBoundary.dimensions.x * 0.5f,
                    QuadTreeBoundary.position.y - QuadTreeBoundary.dimensions.y * 0.5f),
                    new Vector2(QuadTreeBoundary.dimensions.x * 0.5f, QuadTreeBoundary.dimensions.y * 0.5f)));

            splitNodes[2] = new QuadTree(cellCapacity, new Rectangle(new Vector2(QuadTreeBoundary.position.x + QuadTreeBoundary.dimensions.x * 0.5f,
                    QuadTreeBoundary.position.y + QuadTreeBoundary.dimensions.y * 0.5f),
                    new Vector2(QuadTreeBoundary.dimensions.x * 0.5f, QuadTreeBoundary.dimensions.y * 0.5f)));

            splitNodes[3] = new QuadTree(cellCapacity, new Rectangle(new Vector2(QuadTreeBoundary.position.x - QuadTreeBoundary.dimensions.x * 0.5f,
                    QuadTreeBoundary.position.y + QuadTreeBoundary.dimensions.y * 0.5f),
                    new Vector2(QuadTreeBoundary.dimensions.x * 0.5f, QuadTreeBoundary.dimensions.y * 0.5f)));

            for (int i = 0; i < 4; i++)
            {
                splitNodes[i].CurrDepth = this.CurrDepth + 1;

                for (Collider collider : colliders)
                {
                    splitNodes[i].AddCollider(collider);
                }
            }

            this.IsDivided = true;  //ensure that this node cannot be divided again (this will reset the child nodes)
            return;
        }

        if (IsDivided) {
            for (int i = 0; i < 4; i++) {
                splitNodes[i].AddCollider(col);
            }
        }

    }

    //recursive function to gather all the colliders for a specified collider to check against
    public List<Collider> Query(Collider col)
    {
        //create a new empty list
        List<Collider> collidersToCheck = new ArrayList<>();

        //if the collider exists in the collider to check, we begin the recursion
        if (colliders.contains(col)) {
            //this is a leaf node, we can directly add the collider data into the list
            if (!this.IsDivided) {
                //ensure we do not add any duplicates
                for (Collider collider : colliders) {
                    if (collidersToCheck.contains(collider)) { continue; }
                    collidersToCheck.add(collider);
                }

                collidersToCheck.remove(col);
            } else {
                //check the leaf nodes of this cell and query each cell
                for (int i = 0; i < 4; i++) {
                    //ensure we do not add any duplicates
                    for (Collider collider : splitNodes[i].Query(col)) {
                        if (collidersToCheck.contains(collider)) { continue; }
                        collidersToCheck.add(collider);
                    }
                }
            }
        }

        return collidersToCheck;
    }

    public Vector2 GetStartPosition()
    {
        return QuadTreeBoundary.position;
    }

    public Vector2 GetDimensions()
    {
        return QuadTreeBoundary.dimensions;
    }

    public void Render()
    {
        if (this.IsDivided)
        {
            for (int i = 0; i < 4; i++)
            {
                splitNodes[i].Render();
            }
            return;
        }

        Gizmos.DrawBox(QuadTreeBoundary.position, QuadTreeBoundary.dimensions, Color.RED);
    }

}
