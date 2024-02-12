exports.genMainPage = function(tasks, date){
    var pagHTML = `
    <!DOCTYPE html>
    <html>
        <head>
            <meta charset="UTF-8"/>
            <link rel="icon" href="icon_checklist.png"/>
            <link rel="stylesheet" href="w3.css"/>
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
            <title>Task Manager</title>
        </head>
        <body class="w3-light-grey">
            <div class="w3-card-4">
                <header class="w3-container w3-blue-grey">
                    <h1 align="center"><strong>Task Manager</strong></h1>
                </header>
                <br/>
                <form class="w3-container" method="POST">
                    <fieldset>
                        <legend><strong>TASK</strong></legend>
                        <div style="display: flex;">
                            <label class="w3-padding">Deadline:</label>
                            <input class="w3-input w3-round" type="date" name="due_date" required/>
                            <label class="w3-padding">Author:</label>
                            <input list="authors" class="w3-input w3-round" type="text" name="who" required/>
                                <datalist id="authors">`
        var holder = []
        for (let i=0; i<tasks.length; i++){
            if (!(holder.includes(tasks[i].who))){
                pagHTML += `
                                    <option value="${tasks[i].who}">
                `
                holder.push(tasks[i].who)
            }
        }
        pagHTML += `            </datalist>
                        </div>
                        <br/>
                        <div>
                            <label class="w3-padding">Description</label>
                            <input list="desc" class="w3-input w3-round" type="text" name="what" required/>
                                <datalist id="desc">`
        var holder = []
        for (let i=0; i<tasks.length; i++){
            if (!(holder.includes(tasks[i].what))){
                pagHTML += `
                                    <option value="${tasks[i].what}">
                `
                holder.push(tasks[i].what)
            }
        }
        pagHTML += `            </datalist>

                        </div>
                    </fieldset>
                    <div class="w3-padding w3-left">
                        <button class="w3-button w3-round-large w3-blue-grey w3-large">Register</button>
                    </div>
                
                </form>
            </div>
            <br>
            <div class="w3-row-padding w3-padding w3-card-4" name="content">
                <div class="w3-half w3-center w3-card w3-row-padding">
                    <div class="w3-row-padding w3-blue-grey">
                        <h3><strong>TO DO</strong></h3>
                    </div>
                    <div>
                        <table class="w3-table-all w3-centered">
                            <tr>
                                <th>Deadline</th>
                                <th>Description</th>
                                <th>Author</th>
                                <th>Actions</th>
                            </tr>`
    for(let i=0; i<tasks.length; i++){
        if(tasks[i].done == 0){
            pagHTML += `
                            <tr>
                                <td style="vertical-align: middle">${tasks[i].due_date}</td>
                                <td style="vertical-align: middle">${tasks[i].what}</td>
                                <td style="vertical-align: middle">${tasks[i].who}</td>
                                <td>
                                    <div>
                                        <button class="w3-button w3-round-large w3-blue-grey " type="submit"><a style="text-decoration: none" href="/tasks/edit/${tasks[i].id}"><i class="fa fa-pencil"></i></button>
                                        <button class="w3-button w3-round-large w3-green " type="submit"><a style="text-decoration: none" href="/tasks/done/${tasks[i].id}"><i class="fa fa-check"></i></button>                       
                                        <button class="w3-button w3-round-large w3-red " type="submit"><a style="text-decoration: none" href="/tasks/delete/${tasks[i].id}"><i class="fa fa-times"></i></button>
                                    </div>                       
                                </td>
                            </tr>

            `
        }
    }

    pagHTML += `
                        </table>
                    </div>
                </div>
                
                <div class="w3-half w3-center w3-card w3-row-padding">
                    <div class="w3-row-padding w3-blue-grey">
                    <h3><strong>DONE</strong></h3>
                    </div>
                    <div>
                        <table class="w3-table-all w3-centered">
                            <tr>
                                <th>Deadline</th>
                                <th>Description</th>
                                <th>Author</th>
                                <th>Actions</th>
                            </tr>`
    for(let i=0; i<tasks.length; i++){
        if(tasks[i].done == 1){
            pagHTML += `
                            <tr>
                                <td style="vertical-align: middle">${tasks[i].due_date}</td>
                                <td style="vertical-align: middle">${tasks[i].what}</td>
                                <td style="vertical-align: middle">${tasks[i].who}</td>
                                <td>
                                    <div>
                                        <button class="w3-button w3-round-large w3-blue-grey " type="submit"><a style="text-decoration: none" href="/tasks/undo/${tasks[i].id}"><i class="fa fa-undo"></i></button>                    
                                        <button class="w3-button w3-round-large w3-red " type="submit"><a style="text-decoration: none" href="/tasks/delete/${tasks[i].id}"><i class="fa fa-times"></i></button>
                                    </div>                       
                                </td>
                            </tr>

            `
        }
    }

    pagHTML += `
                        </table>
                    </div>
                </div>
            </div>

        </body>
    </html>
    `
    return pagHTML
}

exports.genEditPage = function(task, date){
    return `
    <!DOCTYPE html>
    <html>
        <head>
            <meta charset="UTF-8"/>
            <link rel="icon" href="icon_checklist.png"/>
            <link rel="stylesheet" href="w3.css"/>
            <title>Task Manager</title>
        </head>
        <body class="w3-light-grey">
            <div class="w3-card-4">
                <header class="w3-container w3-blue-grey">
                    <h1 align="center"><strong>Task Manager</strong></h1>
                </header>
                <br/>
                <form class="w3-container" method="POST">
                    <fieldset>
                        <legend><strong>EDIT TASK</strong></legend>
                        <div style="display: flex;">
                            <label class="w3-padding">Deadline:</label>
                            <input class="w3-input w3-round" type="date" name="due_date" value="${task.due_date}" required/>
                            <label class="w3-padding">Author:</label>
                            <input class="w3-input w3-round" type="text" name="who" value="${task.who}" required/>
                        </div>
                        <br/>
                        <div>
                            <label class="w3-padding">Description</label>
                            <input class="w3-input w3-round" type="text" name="what" value="${task.what}" required/>
                        </div>
                    </fieldset>
                    
                    <div class="w3-padding w3-left">
                        <button class="w3-button w3-round-large w3-blue-grey w3-large">Register</button>
                    </div>
                </form>
            </div>

            <footer class="w3-footer w3-bottom w3-padding-small w3-blue-grey">
                <h5 align="center">Task Manager - TPC4 - EngWeb2023 in ${date} - [<a href="/">Main Page</a>]</h5>
            </footer>
        </body>
    </html>
    `
    
}