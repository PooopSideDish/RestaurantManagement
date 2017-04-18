# SideDish POOOP project

### TODO:
- [ ] Menu System
    - [x] add / edit / remove Menu Items in database
    - [ ] Add in auto-complete text system

- [ ] Table
   - [x] add / edit / remove Table in database
   - [ ] Add new order to table
   - [x] Edit or remove an item on the order
   - [x] Add new item to an existing order
   - [ ] Submit order to database

- [x] Users
   - [x] add / edit / remove Users in database
   - [x] Integrate log in with database

- [ ] Kitchen view
   - [ ] Pull current orders from database and display in order
   - [ ] Set order status/flag

- [ ] Statistics menu
   - [ ] Display # of orders over period of time, rank by number (ie: show top 5)
    

- [ ] Make everything purdy


- [ ] Waiters shall be able to place orders into a queue for future interaction
- [ ] The program shall be able to uniquely distinguish between each order placed
- [ ] Waiters/Managers shall be able to modify orders after they are in queue
- [ ] The Kitchen staff shall be able to set items in the queue to the ready position
- [ ] Waiters/Managers shall be able to remove ‘ready’ items from the queue
- [ ] The system shall log the number of each menu item sold in each of the past 7 days, on average for each day of the week, and total sold all time.
- [ ] Restaurant managers shall be able to modify/add menu items
- [ ] Menu items shall be toggleable to determine visibility
- [ ] The system shall have data inputs of new orders, update status of those orders, add new reservations and have data outputs of receipts and sales manifests.
- [ ] The system shall have dates/times in the format of MM/DD/YYYY, HH:MM AM/PM. It will have prices for the menu items in the format $XX.XX (float). It will have employee IDs, and table numbers in integer format. All data will have to be accurate in order to properly work.
- [ ] The system shall be able to asynchronously create as many table reservations (and orders and bills/receipts) as needed. The system shall create a sales manifest once per day, synchronously based on the computer clock.
- [ ] The system shall be able to display the current orders in the queue.
- [ ] There shall be three different types of users: wait staff, kitchen manager, and restaurant manager
- [ ] The price of the menu items shall be a floating point number with two decimal precision ($XX.XX)
- [ ] The formula to determine price trends will total number of sales for that item across a certain time period (weekly, monthly)
- [ ] The database shall retain price information for every menu item. If a menu item is deleted, that information will be removed.
- [ ] The database shall retain information on previously sold orders.
- [ ] Access to each user’s tasks must be controlled by requiring each user to log in
- [ ] All users shall have a unique user ID and password to prevent other employees from accessing sections of the app that they don’t have permission for.
