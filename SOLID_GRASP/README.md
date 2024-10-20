# SOLID/GRASP Task Management System

This project is an updated SOLID/GRASP activity with the following:

## Problem Statement
Design a task management system while keeping the **SOLID** and **GRASP** principles at the forefront of your considerations.

## System Requirements

### Project
- A **project** is a collection of related tasks.
- Each project should have the following attributes:
  - **Name**: The title of the project.
  - **Description**: A brief explanation of the project.
  - **Start Date**: The date the project begins.
  - **End Date**: The date the project is expected to end.
- The project should allow for:
  - Adding/removing **tasks**.
  - Adding/removing **team members**.

### Task
- Each **task** within a project has the following attributes:
  - **Title**: The task's name.
  - **Description**: A short explanation of the task.
  - **Due Date**: The deadline for completing the task.
  - **Status**: The current state of the task (e.g., ToDo, InProgress, Done).
  - **Priority**: The importance level of the task (e.g., Low, Medium, High).

- Different types of tasks may arise:
    - **Recurring tasks** that need to be performed regularly.
    - **High priority tasks** that require immediate attention.

### TeamMember
- Each **team member** has the following attributes:
  - **Name**: The team member’s name.
  - **Email Address**: The contact email for the team member.

- Team members can:
  - **Join** or **leave** projects.