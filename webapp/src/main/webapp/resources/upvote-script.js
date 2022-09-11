function handleClick(img) {
    const src = img.src;
    var number;
    img.parentElement.childNodes.forEach(e => {
        if (e.id === "rating")
            number = e
    })
    console.log(number)
    var newSrc
    if (src.includes('upvote')) {
        if (src.includes('clicked')) {
            // currently upvoted -> remove upvote
            newSrc = src.replace(/-clicked/g, "");
            number.className = ''

        }
        else {
            // currently not upvoted -> upvote
            const aux = src.split('.')
            newSrc = aux[0] + '-clicked' + '.' + aux[1]
            var downvote;
            img.parentElement.childNodes.forEach(e => {
                if (e.id === "downvote")
                    downvote = e
            })
            const otherSrc = downvote.src;
            downvote.setAttribute('src', otherSrc.replace(/-clicked/g, ""));
            number.className = 'upvoted'
        }

    }
    else {
        if (src.includes('clicked')) {
            // currently downvoted -> remove downvote
            newSrc = src.replace(/-clicked/g, "");
            number.className = ''
        }
        else {
            // currently not downvoted -> downvote
            var aux = src.split('.')
            newSrc = aux[0] + '-clicked' + '.' + aux[1]
            var upvote;
            img.parentElement.childNodes.forEach(e => {
                if (e.id === "upvote")
                    upvote = e
            })
            const otherSrc = upvote.src
            upvote.setAttribute('src', otherSrc.replace(/-clicked/g, ""))
            number.className = 'downvoted'
        }
    }
    img.src = newSrc
}
