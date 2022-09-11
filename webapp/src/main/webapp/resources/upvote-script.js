async function postData(url = '', data = {}) {
    // Default options are marked with *
    const response = await fetch(url, {
        method: 'POST', // *GET, POST, PUT, DELETE, etc.
        mode: 'cors', // no-cors, *cors, same-origin
        cache: 'no-cache', // *default, no-cache, reload, force-cache, only-if-cached
        credentials: 'same-origin', // include, *same-origin, omit
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data) // body data type must match "Content-Type" header
    });
    let text = await response.text()
    console.log(text)
    return JSON.parse(text); // parses JSON response into native JavaScript objects
}

async function handleClick(img) {
    const src = img.src;
    const URL = img.getAttribute('url');
    console.log(img)
    const newsId = img.parentElement.newsId;
    var number;
    img.parentElement.childNodes.forEach(e => {
        if (e.id === "rating")
            number = e
    })
    var newSrc
    if (src.includes('upvote')) {
        if (src.includes('clicked')) {
            // currently upvoted -> remove upvote
            console.log(URL)
            postData(URL, {active: false, newsId: newsId}).then(r => {
                if (!r.active) {
                    newSrc = src.replace(/-clicked/g, "");
                    img.src = newSrc
                    number.className = ''
                    number.textContent = r.upvotes
                }
            })
        }
        else {
            // currently not upvoted -> upvote
            postData(URL, {active: true, newsId: newsId}).then(r => {
                console.log('hhhhh' + r.active)
                if (r.active) {
                    const aux = src.split('.')
                    newSrc = aux[0] + '-clicked' + '.' + aux[1]
                    img.src = newSrc
                    var downvote;
                    img.parentElement.childNodes.forEach(e => {
                        if (e.id === "downvote")
                            downvote = e
                    })
                    const otherSrc = downvote.src;
                    downvote.setAttribute('src', otherSrc.replace(/-clicked/g, ""));
                    number.className = 'upvoted'
                    number.textContent = r.upvotes
                }
            })

        }

    }
    else {
        if (src.includes('clicked')) {
            // currently downvoted -> remove downvote
            postData(URL, {active: false, newsId: newsId}).then(r => {
                if (!r.active) {
                    newSrc = src.replace(/-clicked/g, "");
                    img.src = newSrc
                    number.className = ''
                    number.textContent = r.upvotes
                }
            })
        }
        else {
            // currently not downvoted -> downvote
            postData(URL, {active: true, newsId: newsId}).then(r => {
                if (r.active) {
                    var aux = src.split('.')
                    newSrc = aux[0] + '-clicked' + '.' + aux[1]
                    img.src = newSrc
                    var upvote;
                    img.parentElement.childNodes.forEach(e => {
                        if (e.id === "upvote")
                            upvote = e
                    })
                    const otherSrc = upvote.src;
                    upvote.setAttribute('src', otherSrc.replace(/-clicked/g, ""));
                    number.className = 'downvoted'
                    number.textContent = r.upvotes
                }
            })
        }
    }
}
