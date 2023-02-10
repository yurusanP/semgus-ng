package org.semgusng.pretty

import org.semgusng.pretty.base.Code

/**
 * Overload the [codify] function to be pretty printed.
 *
 * Laws:
 *   0. The output [Code] should be pretty. :)
 */
interface Pretty {
  /**
   * Converts to code such that it can be pretty printed.
   */
  fun codify(): Code
}
