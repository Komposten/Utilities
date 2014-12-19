package komposten.utilities.tools;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import sbasicgui.events.SEvent;


/**
 * <code>InputMapper</code> is a class used to allow dynamic re-binding of
 * key/mouse mappings. Actions, in the shape of <code>int</code> codes are
 * stored together with the key and mouse buttons that should invoke that
 * particular action. When an input event is received passing the key code,
 * mouse button code or mouse wheel rotation from the event to
 * {@link #getKeyMapping(int)} (or the mouse button and mouse wheel equivalents)
 * will return all actions associated with that particular input.
 * <br />
 * <br />
 * Multiple actions can be bound to the same key/button, and multiple
 * keys/buttons can be bound to the same action.
 * <br />
 * <br />
 * <b>Note:</b> This class is built for SBasicGUI.
 * 
 * @author Jakob Hjelm
 */
public class InputMapper
{
  public static final int MOUSE1           = -1;
  public static final int MOUSE2           = -2;
  public static final int MOUSE3           = -3;
  public static final int MOUSE_WHEEL_UP   = -4;
  public static final int MOUSE_WHEEL_DOWN = -5;
  public static final int INVALID_CODE    = -10;
  
  private HashMap<Integer, Integer[]> mappings_;
  
  
  {
    mappings_ = new HashMap<Integer, Integer[]>();
  }
  
  

  /**
   * Converts {@link SEvent}'s mouse button constants to
   * <code>InputMapper</code>'s equivalent.
   * 
   * @param mouseButton The mouse button value from an <code>SMouseEvent</code>.
   *          Combined values must be separated and sent to this method
   *          individually.
   * @return <code>InputMapper</code>'s equivalent to the provided
   *         <code>SEvent</code> mouse constant, or {@link #INVALID_CODE} if
   *         <code>mouseButton</code> was invalid.
   */
  public static int getMouseButtonCode(int mouseButton)
  {
    switch (mouseButton)
    {
      case SEvent.MOUSE_BUTTON_LEFT :
        return MOUSE1;
      case SEvent.MOUSE_BUTTON_RIGHT :
        return MOUSE2;
      case SEvent.MOUSE_BUTTON_MIDDLE :
        return MOUSE3;
      default :
        return INVALID_CODE;
    }
  }
  
  

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
  public HashMap<Integer, Integer[]> getMappings()
  {
    return mappings_;
  }
  
  
  
  /**
   * Maps the specified action to the specified key.
   * @param keyCode The key to map the action to.
   * @param action The action code.
   */
  public void registerKey(int keyCode, int action)
  {
    if (keyCode == INVALID_CODE)
      return;
    
    Integer   key     = Integer.valueOf(keyCode);
    Integer[] actions = mappings_.get(key);
    
    if (actions == null || actions.length == 0)
    {
      mappings_.put(key, new Integer[] { action });
    }
    else
    {
      boolean  actionExists = false;
      
      for (Integer s : actions)
        if (s.intValue() == action)
          actionExists = true;
      
      if (!actionExists) //Only add the action if it is not already mapped to this key.
      {
        Integer[] newArray = new Integer[actions.length + 1];
        
        for (int i = 0; i < actions.length; i++)
          newArray[i] = actions[i];
        newArray[actions.length] = action;
        
        mappings_.put(key, newArray);
      }
    }
  }
  
  
  
  /**
   * Maps the specified action to the specified mouse button.
   * <br />
   * <br />
   * This is a convenience method to match {@link SEvent}'s
   * <code>MOUSE_BUTTON</code> constants to <code>InputMapper</code>'s
   * <code>MOUSE</code> constants. Mouse buttons can be registered directly
   * using {@link #registerKey(int, String)} where the <code>int</code>
   * parameter should be the appropriate <code>MOUSE</code> constant (found in
   * this class).
   * 
   * @param buttonCode The mouse button to map the action to.
   * @param action The action code.
   */
  public void registerMouseButton(int buttonCode, Integer action)
  {
    registerKey(getMouseButtonCode(buttonCode), action);
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
  public void registerMouseWheel(int direction, Integer action)
  {
    registerKey(getMouseWheelCode(direction), action);
  }
  
  
  
  /**
   * Removes all key mappings for the specified key code.
   * @param keyCode The key code for the key to remove.
   * @return True if any mappings were found and removed, false otherwise.
   */
  public boolean removeKey(int keyCode)
  {
    if (mappings_.remove(Integer.valueOf(keyCode)) != null)
        return true;
    
    return false;
  }
  


  /**
   * Removes all mouse mappings for the specified mouse button code.
   * <br />
   * <br />
   * This is a convenience method to match {@link SEvent}'s
   * <code>MOUSE_BUTTON</code> constants to <code>InputMapper</code>'s
   * <code>MOUSE</code> constants. Mouse button mappings can be removed directly
   * using {@link #removeKey(int)} where the <code>int</code> parameter should
   * be the appropriate <code>MOUSE</code> constant (found in this class).
   * 
   * @param buttonCode The mouse button code for the key to remove.
   * @return True if any mappings were found and removed, false otherwise.
   * @see #getMouseButtonCode(int)
   */
  public boolean removeMouseButton(int buttonCode)
  {
    return removeKey(getMouseButtonCode(buttonCode));
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
   * @param action The action code associated with the key/mouse button.
   * @return True if a mapping was found and removed, false otherwise.
   */
  public boolean removeAction(Integer action)
  {
    boolean foundKey = false;
    
    Map<Integer, Integer[]> updatedEntries = new HashMap<Integer, Integer[]>();
    
    Iterator<Entry<Integer, Integer[]>> iterator = mappings_.entrySet().iterator();
    
    while (iterator.hasNext())
    {
      Entry<Integer, Integer[]> entry = iterator.next();

      boolean  contained = false;
      
      for (Integer s : entry.getValue())
        if (s.intValue() == action.intValue())
          contained = true;
      
      if (contained)
      {
        Integer[] newArray = new Integer[entry.getValue().length - 1];
        
        for (int i = 0, j = 0; i < entry.getValue().length; i++, j++)
        {
          if (entry.getValue()[i].intValue() != action)
            newArray[j] = entry.getValue()[i];
          else
            j--;
        }
        
        if (newArray.length > 0)
          updatedEntries.put(entry.getKey(), newArray);
        else
          iterator.remove();
        
        foundKey = true;
      }
    }
    
    mappings_.putAll(updatedEntries);
    
    return foundKey;
  }
  

  
  /**
   * Removes the specified action from the specified key/mouse mapping.
   * @param action The action code associated with the key/mouse button.
   * @param inputCode The input code for the key/mouse button.
   * @return True if a mapping was found and removed, false otherwise.
   */
  public boolean removeAction(Integer action, int inputCode)
  {
    boolean foundKey = false;
    
    Integer[] actions = mappings_.get(Integer.valueOf(inputCode));
    
    if (actions != null)
    {
      boolean contained = false;
      
      for (Integer s : actions)
        if (s.intValue() == action.intValue())
          contained = true;
      
      if (contained)
      {
        Integer[] newArray = new Integer[actions.length - 1];
        
        for (int i = 0, j = 0; i < actions.length; i++, j++)
        {
          if (actions[i].intValue() != action)
            newArray[j] = actions[i];
          else
            j--;
        }
        
        if (newArray.length > 0)
          mappings_.put(Integer.valueOf(inputCode), newArray);
        else
          mappings_.remove(Integer.valueOf(inputCode));
        
        foundKey = true;
      }
    }
    
    return foundKey;
  }
  
  

  /**
   * Returns the actions mapped to the specified key code.
   * 
   * @param keyCode The key code for the pressed key.
   * @return Returns the actions mapped to the specified key code, or an empty array
   *         if no such mappings were found.
   */
  public Integer[] getKeyMapping(int keyCode)
  {
    Integer[] actions = mappings_.get(Integer.valueOf(keyCode));
    
    if (actions == null)
      actions = new Integer[0];
    
    return actions;
  }



  /**
   * Returns the actions mapped to the specified mouse button code.
   * <br />
   * <br />
   * This is a convenience method to match {@link SEvent}'s
   * <code>MOUSE_BUTTON</code> constants to <code>InputMapper</code>'s
   * <code>MOUSE</code> constants. Mouse button mappings can be retrieved directly
   * using {@link #getKeyMapping(int)} where the <code>int</code> parameter should
   * be the appropriate <code>MOUSE</code> constant (found in this class).
   * 
   * @param buttonCode The mouse button code for the pressed mouse button (see
   *          the <code>MOUSE_BUTTON</code> constants in {@link SEvent}.
   *          Combinations of several button codes are not supported.
   * @return Returns the actions mapped to the specified mouse button code, or
   *         an empty array if no such mappings were found.
   * @see #getMouseButtonCode(int)
   */
  public Integer[] getMouseMapping(int buttonCode)
  {
    Integer[] actions = mappings_.get(getMouseButtonCode(buttonCode));
    
    if (actions == null)
      actions = new Integer[0];
    
    return actions;
  }



  /**
   * Returns the actions mapped to the specified mouse button code.
   * <br />
   * <br />
   * This is a convenience method to match mouse wheel rotation to
   * <code>InputMapper</code>'s <code>MOUSE_WHEEL</code> constants. Mouse wheel
   * rotation mappings can be retrieved directly using
   * {@link #getKeyMapping(int)} where the <code>int</code> parameter should
   * be the appropriate <code>MOUSE_WHEEL</code> constant (found in this class).
   * 
   * 
   * @param direction The mouse wheel rotation direction to map the action to
   *          (negative values if the mouse wheel was rotated up or away from
   *          the user, and positive values if the mouse wheel was rotated down
   *          or towards the user).
   * @return Returns the actions mapped to the specified mouse button code, or
   *         an empty array if no such mappings were found.
   * @see #getMouseWheelCode(int);
   */
  public Integer[] getMouseWheelMapping(int direction)
  {
    Integer[] actions =  mappings_.get(getMouseWheelCode(direction));
    
    if (actions == null)
      actions = new Integer[0];
    
    return actions;
  }
}
