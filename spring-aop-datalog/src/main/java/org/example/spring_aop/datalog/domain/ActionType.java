package org.example.spring_aop.datalog.domain;

/**
 * @author lifei
 */
public enum ActionType {
    /**
     * insert
     */
    INSERT("添加", 1),
    /**
     * update
     */
    UPDATE("更新", 2),
    /**
     * delete
     */
    DELETE("删除", 3);
    /**
     * name
     */
    private final String name;
    /**
     * index
     */
    private final int index;

    ActionType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for (ActionType actionType : ActionType.values()) {
            if (actionType.getIndex() == index) {
                return actionType.name;
            }
        }
        return null;
    }

    public static Integer getIndex(String name) {
        for (ActionType actionType : ActionType.values()) {
            if (actionType.getName().equals(name)) {
                return actionType.index;
            }
        }
        return null;
    }

    public static ActionType getActionType(int index) {
        for (ActionType actionType : ActionType.values()) {
            if (actionType.getIndex() == index) {
                return actionType;
            }
        }
        return null;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }
}
