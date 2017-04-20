# SideDish POOOP project

### TODO:
- [x] Update Table Status on order creation and deletion
- [x] Add Order number to order screen
- [ ] Add item status to order screen
- [ ] Add comment to item from order screen
- [ ] Redesign kitchen queue rows to display comments on an item
- [ ] Design kitchen queue rows to be spaced nicely
- [ ] View comment on item from queue screen
- [ ] Change item's status from queue screen
- [ ] Change table table columns to: number, section, status
- [ ] Space out table table rows to match columns
- [ ] Add all items in a submitted order to the history table for statistics
- [ ] Create Statistics Screen and wire it to the nav drawer
- [ ] Statistics date pickers
- [ ] Add confirm deletion dialogs for orders, menu items, and users
- [ ] Ensure that no two menu items have the same name
- [ ] Ensure that no two users have the same id
- [x] Make admin default login for case where app has no users
- [x] Add password checking for login
- [x] Restrict the kitchen staff to only kitchen view
- [x] Restrict the waitstaff to only tables/orders


### Requirements Traceability Matrix


- [x] There shall be three different types of users: wait staff, kitchen manager, and restaurant manager 
- [ ] All users shall have a unique user ID and password to prevent other employees from accessing sections of the app that they don’t have permission for. 
- [x] Access to each user’s tasks must be controlled by requiring each user to log in 
- [ ] The system shall be able to asynchronously create as many table reservations (and orders and bills/receipts) as needed. The system shall create a sales manifest once per day, synchronously based on the computer clock. 
- [ ] Restaurant managers shall be able to modify/add menu items 
- [ ] The price of the menu items shall be a floating point number with two decimal precision ($XX.XX) 
- [ ] The database shall retain price information for every menu item. If a menu item is deleted, that information will be removed. 
- [ ] Menu items shall be toggleable to determine visibility 
- [ ] The system shall have data inputs of new orders, update status of those orders, add new reservations and have data outputs of receipts and sales manifests. 
- [ ] The system shall have dates/times in the format of MM/DD/YYYY, HH:MM AM/PM. It will have employee IDs, and table numbers in integer format. All data will have to be accurate in order to properly work. 
- [x] Waiters shall be able to place orders into a queue for future interaction 
- [ ] Waiters/Managers shall be able to modify orders after they are in queue 
- [x] The program shall be able to uniquely distinguish between each order placed 
- [x] The system shall be able to display the current orders in the queue. 
- [ ] The Kitchen staff shall be able to set items in the queue to the ready position
- [ ] Waiters/Managers shall be able to remove ‘ready’ items from the queue 
- [ ] The database shall retain information on previously sold orders. 
- [ ] The system shall log the number of each menu item sold in each of the past 7 days, on average for each day of the week, and total sold all time. 
- [ ] The formula to determine price trends will total number of sales for that item across a certain time period (weekly, monthly) 
