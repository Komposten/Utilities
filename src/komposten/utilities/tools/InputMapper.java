package komposten.utilities.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

//import sbasicgui.events.SEvent;


/**
 * <code>InputMapper</code> is a class used to allow dynamic re-binding of
 * key/mouse mappings. <i>Actions</i> are
 * stored together with the keys and mouse buttons that should invoke that
 * particular action. When an input event is received the key code,
 * mouse button code or mouse wheel rotation from the event should be passed to
 * {@link #getKeyMappings(int)} (or the mouse button and mouse wheel equivalents)
 * in order to retrieve a list of all actions associated with that particular input.
 * <br />
 * <br />
 * Multiple actions can be bound to the same key/button, and multiple
 * keys/buttons can be bound to the same action.
 * <br />
 * <br />
 * 
 * @author Jakob Hjelm
 * @version
 * <b>1.2.0</b> <br />
 * <ul>
 * <li>Removed SBasicGUI dependency.</li>
 * <li>Updated JavaDoc.</li>
 * <li>Minor refactoring.</li>
 * <li>Added an empty list to be returned instead of using <code>new ArrayList<T>()</code>.
 * </ul>
 * <b>Older</b> <br />
 * 1.1.0 <br />
 * <ul>
 * <li>Made the action code type generic.</li>
 * <li>Added <code>getMappingsForAction(T)</code>.</li>
 * </ul> <br />
 * 1.0.0 <br />
 * <ul>
 * <li>Initial implementation.</li>
 * </ul> <br />
 */
public class InputMapper<T>
{
  public static final int MOUSE1           = -1;
  public static final int MOUSE2           = -2;
  public static final int MOUSE3           = -3;
  public static final int MOUSE_WHEEL_UP   = -4;
  public static final int MOUSE_WHEEL_DOWN = -5;
  public static final int INVALID_CODE    = -10;
  
  private HashMap<Integer, ArrayList<T>> mappings_;
  private ArrayList<T> emptyList_;
  
  
  {
    mappings_  = new HashMap<Integer, ArrayList<T>>();
    emptyList_ = new ArrayList<T>();
  }
  
  

//  /**
//   * Converts {@link SEvent}'s mouse button constants to
//   * <code>InputMapper</code>'s equivalent.
//   * 
//   * @param mouseButton The mouse button value from an <code>SMouseEvent</code>.
//   *          Combined values must be separated and sent to this method
//   *          individually.
//   * @return <code>InputMapper</code>'s equivalent to the provided
//   *         <code>SEvent</code> mouse constant, or {@link #INVALID_CODE} if
//   *         <code>mouseButton</code> was invalid.
//   */
//  public static int getMouseButtonCode(int mouseButton)
//  {
//    switch (mouseButton)
//    {
//      case SEvent.MOUSE_BUTTON_LEFT :
//        return MOUSE1;
//      case SEvent.MOUSE_BUTTON_RIGHT :
//        return MOUSE2;
//      case SEvent.MOUSE_BUTTON_MIDDLE :
//        return MOUSE3;
//      default :
//        return INVALID_CODE;
//    }
//  }
  
  

  /**
   * Converts mouse wheel rotation to the appropriate constant from this class.
   * 
   * @param mouseWheelDirection The mouse wheel rotation direction
   *          (negative values if the mouse wheel was rotated up or away from
   *          the user, and positive values if the mouse wheel was rotated down
   *          or towards the user).
   * @return <code>InputMapper</code>'s equivalent to the provided mouse wheel rotation direction.
   */
  public static int getMouseWheelCode(int mouseWheelDirection)
  {
    if (mouseWheelDirection > 0)
      return MOUSE_WHEEL_DOWN;
    else if (mouseWheelDirection < 0)
      return MOUSE_WHEEL_UP;
    else
      return INVALID_CODE;
  }
  
  
  
  /**
   * Returns the map containing all mappings.
   * @return A map containing all mappings.
   */
  public HashMap<Integer, ArrayList<T>> getMappings()
  {
    return mappings_;
  }
  
  
  
  /**
   * Maps the specified action to the specified key or mouse button press, or mouse wheel rotation.
   * @param keyCode The key to map the action to.
   * @param action The action code.
   */
  public void registerKey(int keyCode, T action)
  {
    if (keyCode == INVALID_CODE)
      return;
    
    Integer      key     = Integer.valueOf(keyCode);
    ArrayList<T> actions = mappings_.get(key);
    
    if (actions == null || actions.size() == 0)
    {
      actions = new ArrayList<T>();
      actions.add(action);
      
      mappings_.put(key, actions);
    }
    else
    {
      if (!actions.contains(action))
        actions.add(action);
    }
  }
  
  
  
  /**
   * Does the same thing as {@link #registerKey(int, Object)}.
   */
  public void registerMouseButton(int buttonCode, T action)
  {
    registerKey(buttonCode, action);
  }
  
  
  
  /**
   * Maps the specified action to the specified mouse button.
   * <br />
   * <br />
   * This is a convenience method to match mouse wheel rotation to
   * <code>InputMapper</code>'s <code>MOUSE_WHEEL</code> constants. Mouse wheel
   * rotation can be registered directly using {@link #registerKey(int, String)}
   * where the <code>int</code> parameter should be the appropriate
   * <code>MOUSE_WHEEL</code> constant (found in this class).
   * 
   * @param direction The mouse wheel rotation direction to map the action to
   *          (negative values if the mouse wheel was rotated up or away from
   *          the user, and positive values if the mouse wheel was rotated down
   *          or towards the user).
   * @param action The action code.
   */
  public void registerMouseWheel(int direction, T action)
  {
    registerKey(getMouseWheelCode(direction), action);
  }
  
  
  
  /**
   * Removes all key mappings for the specified key or mouse code.
   * @param code The key code for the key to remove.
   * @return True if any mappings were found and removed, false otherwise.
   */
  public boolean removeKey(int code)
  {
    if (mappings_.remove(Integer.valueOf(code)) != null)
        return true;
    
    return false;
  }
  


  /**
   * Does the same thing as {@link #removeKey(int)}.
   */
  public boolean removeMouseButton(int buttonCode)
  {
    return removeKey(buttonCode);
  }
  


  /**
   * Removes all mouse wheel mappings for the specified mouse wheel rotation
   * direction.
   * <br />
   * <br />
   * This is a convenience method to match mouse wheel rotation to
   * <code>InputMapper</code>'s <code>MOUSE_WHEEL</code> constants. Mouse wheel
   * rotation mappings can be removed directly using
   * {@link #removeKey(int, String)} where the <code>int</code> parameter should
   * be the appropriate <code>MOUSE_WHEEL</code> constant (found in this class).
   * 
   * 
   * @param direction The mouse wheel rotation direction to map the action to
   *          (negative values if the mouse wheel was rotated up or away from
   *          the user, and positive values if the mouse wheel was rotated down
   *          or towards the user).
   * @return True if any mappings were found and removed, false otherwise.
   * @see #getMouseWheelCode(int)
   */
  public boolean removeMouseWheel(int direction)
  {
    return removeKey(getMouseWheelCode(direction));
  }
  
  
  
  /**
   * Removes the specified action from all key/mouse mappings.
   * @param action The action code associated with the key/mouse button to remove.
   * @return True if a mapping was found and removed, false otherwise.
   */
  public boolean removeAction(T action)
  {
    boolean foundKey = false;
    
    Iterator<Entry<Integer, ArrayList<T>>> iterator = mappings_.entrySet().iterator();
    
    while (iterator.hasNext())
    {
      Entry<Integer, ArrayList<T>> entry = iterator.next();
      
      if (entry.getValue().remove(action))
      {
        if (entry.getValue().size() <= 0)
          iterator.remove();
        
        foundKey = true;
      }
    }
    
    return foundKey;
  }
  

  
  /**
   * Removes the specified action from the specified key/mouse mapping.
   * @param action The action code associated with the key/mouse button.
   * @param inputCode The input code for the key/mouse button.
   * @return True if a mapping was found and removed, false otherwise.
   */
  public boolean removeAction(T action, int inputCode)
  {
    boolean foundKey = false;
    
    ArrayList<T> actions = mappings_.get(Integer.valueOf(inputCode));
    
    if (actions != null)
    {
      if (actions.remove(action))
      {
        if (actions.size() <= 0)
          mappings_.remove(Integer.valueOf(inputCode));
      }
    }
    
    return foundKey;
  }
  
  

	/**
	 * Returns a list of the actions mapped to the specified key code. The list is
	 * backed by the <code>InputMapper</code>, so changes to the list are
	 * reflected in the <code>InputMapper</code>.
	 * 
	 * @param keyCode The key code for the pressed key.
	 * @return The actions mapped to the specified key code, or an empty list if
	 *         no such mappings were found.
	 */
  public ArrayList<T> getKeyMappings(int keyCode)
  {
    ArrayList<T> actions = mappings_.get(Integer.valueOf(keyCode));
    
    if (actions == null)
      actions = emptyList_;
    
    return actions;
  }



  /**
   * Does the same thing as {@link #getKeyMappings(int)}.
   */
  public ArrayList<T> getMouseMappings(int buttonCode)
  {
    return getKeyMappings(buttonCode);
  }
  
  
  
	/**
	 * Returns a list of the actions mapped to the specified mouse button code.
	 * The list is backed by the <code>InputMapper</code>, so changes to the list
	 * are reflected in the <code>InputMapper</code>. <br />
	 * <br />
	 * This is a convenience method to match mouse wheel rotation to
	 * <code>InputMapper</code>'s <code>MOUSE_WHEEL</code> constants. Mouse wheel
	 * rotation mappings can be retrieved directly using
	 * {@link #getKeyMappings(int)} where the <code>int</code> parameter should be
	 * the appropriate <code>MOUSE_WHEEL</code> constant (found in this class).
	 * 
	 * 
	 * @param direction The mouse wheel rotation direction to map the action to
	 *          (negative values if the mouse wheel was rotated up or away from
	 *          the user, and positive values if the mouse wheel was rotated down
	 *          or towards the user).
	 * @return The actions mapped to the specified mouse button code, or an empty
	 *         list if no such mappings were found.
	 * @see #getMouseWheelCode(int);
	 */
  public ArrayList<T> getMouseWheelMappings(int direction)
  {
    return getKeyMappings(getMouseWheelCode(direction));
  }
  
  
  
  /**
   * Returns all keys and buttons associated with the specified action.
   * @param action The action.
   * @return All keys and buttons associated with the specified action.
   */
  public ArrayList<Integer> getMappingsForAction(T action)
  {
    Iterator<Entry<Integer, ArrayList<T>>> iterator = mappings_.entrySet().iterator();
    
    ArrayList<Integer> keysAndButtons = new ArrayList<Integer>();
    
    while (iterator.hasNext())
    {
      Entry<Integer, ArrayList<T>> entry = iterator.next();
      
      if (entry.getValue().contains(action))
        keysAndButtons.add(entry.getKey());
    }
    
    return keysAndButtons;
  }
  
  
  
//  public static void main(String[] args)
//  {
//    InputMapper<Integer> mapper = new InputMapper<>();
//
//    mapper.registerKey(1, 1);
//    mapper.registerKey(1, 2);
//    mapper.registerKey(1, 3);
//    mapper.registerKey(1, 4);
//    mapper.registerKey(2, 1);
//    mapper.registerKey(2, 2);
//    mapper.registerKey(2, 3);
//    mapper.registerKey(2, 4);
//    mapper.registerKey(3, 1);
//    mapper.registerKey(3, 2);
//
//    System.out.println("Registered keys: " + mapper.getMappings().size());
//    System.out.println("Registered actions for key 1: " + mapper.getKeyMappings(1).size());
//    System.out.println("Registered actions for key 2: " + mapper.getKeyMappings(2).size());
//    System.out.println("Removing action 2 from key 1"); mapper.removeAction(2, 1);
//    System.out.println("Registered actions for key 1: " + mapper.getKeyMappings(1).size());
//    System.out.println("Adding action 2 to key 1"); mapper.registerKey(1, 2);
//    System.out.println("Registered actions for key 1: " + mapper.getKeyMappings(1).size());
//    System.out.println("Removing action 2 from all keys"); mapper.removeAction(2);
//    System.out.println("Registered actions for key 1: " + mapper.getKeyMappings(1).size());
//    System.out.println("Registered actions for key 2: " + mapper.getKeyMappings(2).size());
//    System.out.println("Registered keys for action 3: " + mapper.getMappingsForAction(3).size());
//    System.out.println("Removing key 1"); mapper.removeKey(1);
//    System.out.println("Registered actions for key 1: " + mapper.getKeyMappings(1).size());
//  }
}
