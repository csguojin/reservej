local seatKey = KEYS[1]
local userKey = KEYS[2]
local startOffset = tonumber(ARGV[1])
local endOffset = tonumber(ARGV[2])

local seatBitCount = redis.call('BITCOUNT', seatKey, startOffset, endOffset, 'BIT')
local userBitCount = redis.call('BITCOUNT', userKey, startOffset, endOffset, 'BIT')

if seatBitCount == 0 and userBitCount == 0 then
    local bitfieldArgs = {}

    for i = startOffset, endOffset do
        table.insert(bitfieldArgs, "SET")
        table.insert(bitfieldArgs, "u1")
        table.insert(bitfieldArgs, i)
        table.insert(bitfieldArgs, 1)
    end

    redis.call('BITFIELD', seatKey, unpack(bitfieldArgs))
    redis.call('BITFIELD', userKey, unpack(bitfieldArgs))

    return 'OK'
else
    return 'BITCOUNT not equal to 0'
end